package org.xi.maple.scheduler.k8s.service.impl;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.scheduler.k8s.model.K8sClusterQueue;
import org.xi.maple.scheduler.model.ClusterQueue;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.constant.K8sResourceType;
import org.xi.maple.scheduler.constant.MapleConstants;
import org.xi.maple.common.exception.MapleClusterNotConfiguredException;
import org.xi.maple.common.exception.MapleEngineTypeNotSupportException;
import org.xi.maple.common.exception.MapleK8sException;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;
import org.xi.maple.scheduler.k8s.MapleResourceEventHandler;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeploymentList;
import org.xi.maple.scheduler.k8s.flink.eventhandler.FlinkDeploymentEventHandler;
import org.xi.maple.scheduler.k8s.service.K8sClusterService;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplication;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationList;
import org.xi.maple.scheduler.k8s.spark.eventhandler.SparkApplicationEventHandler;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueue;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueueList;
import org.xi.maple.scheduler.model.YarnCluster;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xishihao
 */
@Service("k8sClusterService")
public class K8sClusterServiceImpl implements K8sClusterService {

    private static final Logger logger = LoggerFactory.getLogger(K8sClusterServiceImpl.class);

    private static final Map<String, Method> configSetMethodMap = getConfigSetMethodMap();

    final static Map<String, ClusterQueue> CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();

    private final PersistenceClient client;
    private final UpdateExecStatusFunc updateExecStatusFunc;

    /**
     * 集群名称 -> KubernetesClient
     */
    private final Map<String, KubernetesClient> k8sClients;

    public K8sClusterServiceImpl(PersistenceClient client, UpdateExecStatusFunc updateExecStatusFunc) {
        this.client = client;
        this.updateExecStatusFunc = updateExecStatusFunc;
        this.k8sClients = new HashMap<>();
    }

    /**
     * 动态设置 K8s 配置（暂时没用到）
     *
     * @return
     */
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


    @Override
    public List<HasMetadata> deployEngine(String clusterName, MultipartFile yamlFile) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterName);
        try (InputStream is = yamlFile.getInputStream()) {
            return kubernetesClient.load(is).serverSideApply();
        } catch (Throwable t) {
            throw new MapleK8sException(t);
        }
    }

    @Override
    public List<StatusDetails> deleteEngine(String clusterName, MultipartFile yamlFile) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterName);
        try (InputStream is = yamlFile.getInputStream()) {
            return kubernetesClient.load(is).delete();
        } catch (Throwable t) {
            throw new MapleK8sException(t);
        }
    }

    @Override
    public List<HasMetadata> deployEngine(String clusterName, String yaml) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterName);
        try (InputStream is = new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8))) {
            return kubernetesClient.load(is).serverSideApply();
        } catch (Throwable t) {
            throw new MapleK8sException(t);
        }
    }

    @Override
    public List<StatusDetails> deleteEngine(String clusterName, String yaml) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterName);
        try (InputStream is = new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8))) {
            return kubernetesClient.load(is).delete();
        } catch (Throwable t) {
            throw new MapleK8sException(t);
        }
    }

    @Override
    public List<StatusDetails> deleteEngine(String clusterName, String namespace, String type, String name) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterName);
        try {
            if (K8sResourceType.FLINK.is(type)) {
                return kubernetesClient.resources(FlinkDeployment.class).inNamespace(namespace).withName(name).delete();
            } else if (K8sResourceType.SPARK.is(type)) {
                return kubernetesClient.resources(SparkApplication.class).inNamespace(namespace).withName(name).delete();
            }
        } catch (Throwable t) {
            throw new MapleK8sException(t);
        }
        throw new MapleEngineTypeNotSupportException("engine type not support: " + type);
    }

    private KubernetesClient getKubernetesClient(String clusterName) {
        if (!k8sClients.containsKey(clusterName)) {
            throw new MapleClusterNotConfiguredException("Cluster [" + clusterName + "] not configured");
        }
        return k8sClients.get(clusterName);
    }

    private List<YarnCluster> getClusters() {
        return new ArrayList<>(0);
    }

    public void refreshClusterConfig(String clusterName) {
        ClusterDetailResponse cluster = client.getByName(clusterName);
        if (k8sClients.containsKey(clusterName)) {
            try (KubernetesClient kubernetesClient = k8sClients.remove(clusterName)) {
                kubernetesClient.informers().stopAllRegisteredInformers();
            } catch (Throwable t) {
                logger.error("Close kubernetes client error, clusterName: " + clusterName, t);
            }
        }
        KubernetesClient kubernetesClient = createKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
        k8sClients.put(cluster.getName(), kubernetesClient);
        refreshExecStatus(clusterName, kubernetesClient);
    }

    /**
     * 刷新引擎执行任务状态
     */
    @Scheduled(fixedDelay = 5000)
    public void refreshClusters() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setCategory(ClusterCategoryConstants.K8s);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            KubernetesClient kubernetesClient;
            if (k8sClients.containsKey(cluster.getName())) {
                kubernetesClient = k8sClients.get(cluster.getName());
            } else {
                kubernetesClient = createKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
                k8sClients.put(cluster.getName(), kubernetesClient);
            }
            refreshExecStatus(cluster.getName(), kubernetesClient);
        }
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
    public void refreshExecStatus(String clusterName, KubernetesClient kubernetesClient) {

        SharedIndexInformer<FlinkDeployment> flinkInformer = kubernetesClient
                .resources(FlinkDeployment.class, FlinkDeploymentList.class)
                .withLabel(MapleConstants.LABEL_EXEC_KEY, MapleConstants.LABEL_EXEC_VALUE)
                .inform(new FlinkDeploymentEventHandler(updateExecStatusFunc), 10000L);

        flinkInformer.start();

        SharedIndexInformer<SparkApplication> sparkInformer = kubernetesClient
                .resources(SparkApplication.class, SparkApplicationList.class)
                .withLabel(MapleConstants.LABEL_EXEC_KEY, MapleConstants.LABEL_EXEC_VALUE)
                .inform(new SparkApplicationEventHandler(updateExecStatusFunc), 10000L);
        sparkInformer.start();

        SharedIndexInformer<VolcanoQueue> volcanoInformer = kubernetesClient
                .resources(VolcanoQueue.class, VolcanoQueueList.class)
                .inform(new MapleResourceEventHandler<>() {
                    @Override
                    public void onAdd(VolcanoQueue volcanoQueue) {
                        String key = ClusterQueue.getClusterQueueKey(clusterName, volcanoQueue.getMetadata().getName());
                        ClusterQueue clusterQueue = new K8sClusterQueue(volcanoQueue.getStatus().getPending());
                        CLUSTER_QUEUE_MAP.put(key, clusterQueue);
                    }

                    @Override
                    public void onUpdate(VolcanoQueue oldVolcanoQueue, VolcanoQueue volcanoQueue) {
                        String key = ClusterQueue.getClusterQueueKey(clusterName, volcanoQueue.getMetadata().getName());
                        ClusterQueue clusterQueue = new K8sClusterQueue(volcanoQueue.getStatus().getPending());
                        CLUSTER_QUEUE_MAP.put(key, clusterQueue);
                    }

                    @Override
                    public void onDelete(VolcanoQueue volcanoQueue, boolean deletedFinalStateUnknown) {
                        String key = ClusterQueue.getClusterQueueKey(clusterName, volcanoQueue.getMetadata().getName());
                        CLUSTER_QUEUE_MAP.remove(key);
                    }
                }, 10000L);
        volcanoInformer.start();

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

    @Override
    public ClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        return CLUSTER_QUEUE_MAP.getOrDefault(ClusterQueue.getClusterQueueKey(clusterName, queue), null);
    }

    @Data
    public static class KubeConfigWithType {
        private String type;
        private String kubeConfigFile;
        private Config config;
    }

    /**
     * 根据配置的 master 和 configJson 获取 KubernetesClient
     *
     * @param master     地址
     * @param configJson 配置
     * @return KubernetesClient
     */
    private KubernetesClient createKubernetesClient(String master, String configJson) {
        KubeConfigWithType kubeConfig = JsonUtils.parseObject(configJson, KubeConfigWithType.class, null);
        if (kubeConfig == null) {
            Config config = new ConfigBuilder().withMasterUrl(master).build();
            return new KubernetesClientBuilder().withConfig(config).build();
        }
        if ("file".equals(kubeConfig.getType())) {
            if (StringUtils.isNotBlank(kubeConfig.getKubeConfigFile())) {
                Config config = Config.fromKubeconfig(null, null, kubeConfig.getKubeConfigFile());
                return new KubernetesClientBuilder().withConfig(config).build();
            }
        } else {
            Config config = kubeConfig.getConfig();
            config.setMasterUrl(master);
            return new KubernetesClientBuilder().withConfig(config).build();
        }

        Config config = new ConfigBuilder().withMasterUrl(master).build();
        return new KubernetesClientBuilder().withConfig(config).build();
    }
}
