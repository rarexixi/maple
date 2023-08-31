package org.xi.maple.execution.k8s;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xi.maple.common.constant.ClusterTypeConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeploymentList;
import org.xi.maple.execution.k8s.flink.eventhandler.FlinkDeploymentEventHandler;
import org.xi.maple.execution.k8s.spark.crds.SparkDeployment;
import org.xi.maple.execution.k8s.spark.crds.SparkDeploymentList;
import org.xi.maple.execution.k8s.spark.eventhandler.SparkDeploymentEventHandler;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EngineInformer {

    private static final Logger logger = LoggerFactory.getLogger(EngineInformer.class);

    private static final String LABEL_EXEC = "maple-exec";
    private static final String LABEL_ID = "maple-id";

    private static final Map<String, Method> configSetMethodMap = getConfigSetMethodMap();

    /**
     * 集群名称 -> KubernetesClient
     */
    private Map<String, KubernetesClient> k8sClients = new HashMap<>();

    private final PersistenceClient client;

    public EngineInformer(PersistenceClient client) {
        this.client = client;
    }

    private static Map<String, Method> getConfigSetMethodMap() {
        Map<String, Method> methodMap = new HashMap<>();
        Method[] methods = Config.class.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                methodMap.put(method.getName(), method);
            }
        }
        return methodMap;
    }


    public void refreshClusterConfig(String name) {
        ClusterDetailResponse cluster = client.getByName(name);

        KubernetesClient kubernetesClient;
        if (k8sClients.containsKey(cluster.getName())) {
            kubernetesClient = k8sClients.remove(name);
            kubernetesClient.informers().stopAllRegisteredInformers();
            kubernetesClient.close();
        }
        kubernetesClient = getKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
        k8sClients.put(cluster.getName(), kubernetesClient);
        refreshExecStatus(kubernetesClient);
    }

    /**
     * 刷新引擎执行任务状态
     */
    @Scheduled
    public void refreshClusters() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setType(ClusterTypeConstants.K8s);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            KubernetesClient kubernetesClient;
            if (k8sClients.containsKey(cluster.getName())) {
                kubernetesClient = k8sClients.get(cluster.getName());
            } else {
                kubernetesClient = getKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
                k8sClients.put(cluster.getName(), kubernetesClient);
            }
            refreshExecStatus(kubernetesClient);
        }
    }

    /**
     * 根据配置的 master 和 configJson 获取 KubernetesClient
     *
     * @param master     地址
     * @param configJson 配置
     * @return KubernetesClient
     */
    private KubernetesClient getKubernetesClient(String master, String configJson) {
        Config config = new ConfigBuilder().withMasterUrl(master).build();
        Map<String, Object> map = JsonUtils.parseObject(configJson, Map.class, null);
        if (map == null) {
            return new KubernetesClientBuilder().withConfig(config).build();
        }
        for (String key : map.keySet()) {
            String methodName = "set" + kebabToPascal(key);
            if (!configSetMethodMap.containsKey(methodName)) {
                continue;
            }
            Method method = configSetMethodMap.get(methodName);
            try {
                Object parameter = JsonUtils.convertValue(map.get(key), method.getParameterTypes()[0], null);
                if (parameter != null) {
                    method.invoke(config, parameter);
                }
            } catch (Exception e) {
                logger.error("set config error", e);
            }
        }
        return new KubernetesClientBuilder().withConfig(config).build();
    }

    /**
     * kebab-case to pascal-case
     *
     * @param content 源字符串
     * @return 大写驼峰字符串
     */
    public static String kebabToPascal(String content) {
        if (null == content) return null;
        int contentLength = content.length();
        if (contentLength == 0) return content;

        StringBuilder stringBuilder = new StringBuilder(contentLength);
        char[] chars = content.toCharArray();
        stringBuilder.append(Character.toUpperCase(chars[0]));
        for (int i = 1; i < contentLength; i++) {
            if (chars[i] == '-') {
                if (++i < contentLength) {
                    stringBuilder.append(Character.toUpperCase(chars[i]));
                }
            } else {
                stringBuilder.append(chars[i]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 刷新引擎执行任务状态
     *
     * @param kubernetesClient
     */
    public void refreshExecStatus(KubernetesClient kubernetesClient) {

        SharedIndexInformer<FlinkDeployment> flinkInformer = kubernetesClient
                .resources(FlinkDeployment.class, FlinkDeploymentList.class)
                .withLabel("from-app", LABEL_EXEC)
                .inform(new FlinkDeploymentEventHandler(), 10000L);

        flinkInformer.start();

        SharedIndexInformer<SparkDeployment> sparkInformer = kubernetesClient
                .resources(SparkDeployment.class, SparkDeploymentList.class)
                .withLabel("from-app", LABEL_EXEC)
                .inform(new SparkDeploymentEventHandler(), 10000L);
        sparkInformer.start();
    }

    private void close() {
        for (KubernetesClient kubernetesClient : k8sClients.values()) {
            kubernetesClient.close();
            kubernetesClient.informers().stopAllRegisteredInformers();
        }
    }

    @PostConstruct
    public void run() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }
}
