package org.xi.maple.scheduler.k8s.service.impl;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.exception.MapleClusterConfigException;
import org.xi.maple.common.exception.MapleClusterNotConfiguredException;
import org.xi.maple.common.exception.MapleEngineTypeNotSupportException;
import org.xi.maple.common.exception.MapleK8sException;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.constant.K8sResourceType;
import org.xi.maple.scheduler.constant.MapleConstants;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;
import org.xi.maple.scheduler.k8s.MapleResourceEventHandler;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeploymentList;
import org.xi.maple.scheduler.k8s.flink.eventhandler.FlinkDeploymentEventHandler;
import org.xi.maple.scheduler.k8s.model.K8sClusterQueue;
import org.xi.maple.scheduler.k8s.model.KubeConfigWithType;
import org.xi.maple.scheduler.k8s.service.K8sClusterService;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplication;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationList;
import org.xi.maple.scheduler.k8s.spark.eventhandler.SparkApplicationEventHandler;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueue;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueueList;
import org.xi.maple.scheduler.model.ClusterQueue;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
        throw new MapleEngineTypeNotSupportException("不支持的引擎类型: " + type);
    }

    private KubernetesClient getKubernetesClient(String clusterName) {
        if (!k8sClients.containsKey(clusterName)) {
            throw new MapleClusterNotConfiguredException("K8s 集群 [" + clusterName + "] 没有被配置");
        }
        return k8sClients.get(clusterName);
    }

    @Override
    public void removeClusterConfig(String clusterName) {
        if (k8sClients.containsKey(clusterName)) {
            try (KubernetesClient kubernetesClient = k8sClients.remove(clusterName)) {
                kubernetesClient.informers().stopAllRegisteredInformers();
            } catch (Throwable t) {
                logger.error("关闭 K8s 客户端错误, name: {}", clusterName, t);
            }
        }
    }

    @Override
    public void addClusterConfig(ClusterDetailResponse cluster) {
        KubernetesClient kubernetesClient = createKubernetesClient(cluster);
        k8sClients.put(cluster.getName(), kubernetesClient);
        refreshExecStatus(cluster.getName(), kubernetesClient);
    }

    @Override
    public void refreshAllClusterConfig() {

    }

    /**
     * 刷新 K8s 集群配置，不会重启已有集群，如果已有集群有变更，需要使用强制刷新
     */
    // @Scheduled(fixedDelay = 5000)
    public void refreshClusters() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setCategory(ClusterCategoryConstants.K8s);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        final Set<String> clusterNames = new HashSet<>(clusters.size());
        for (ClusterListItemResponse cluster : clusters) {
            clusterNames.add(cluster.getName());
            KubernetesClient kubernetesClient;
            if (k8sClients.containsKey(cluster.getName())) {
                kubernetesClient = k8sClients.get(cluster.getName());
            } else {
                kubernetesClient = createKubernetesClient(cluster);
                k8sClients.put(cluster.getName(), kubernetesClient);
            }
            refreshExecStatus(cluster.getName(), kubernetesClient);
        }
        for (String clusterName : k8sClients.keySet()) {
            if (!clusterNames.contains(clusterName)) {
                removeClusterConfig(clusterName);
            }
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
     * @param clusterName      集群名称
     * @param kubernetesClient K8s client
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

    @PostConstruct
    public void run() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (KubernetesClient kubernetesClient : k8sClients.values()) {
                kubernetesClient.informers().stopAllRegisteredInformers();
                kubernetesClient.close();
            }
        }));
    }

    @Override
    public ClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        return CLUSTER_QUEUE_MAP.getOrDefault(ClusterQueue.getClusterQueueKey(clusterName, queue), null);
    }


    /**
     * 根据配置的 master 和 configJson 获取 KubernetesClient
     *
     * @param cluster 集群配置
     * @return KubernetesClient
     */
    private KubernetesClient createKubernetesClient(ClusterListItemResponse cluster) {
        Config config = getConfig(cluster);
        return new KubernetesClientBuilder().withConfig(config).build();
    }

    private Config getConfig(ClusterListItemResponse cluster) {
        String name = cluster.getName();
        String master = cluster.getAddress();
        String configJson = cluster.getConfiguration();
        KubeConfigWithType kubeConfig = JsonUtils.parseObject(configJson, KubeConfigWithType.class, null);
        if (kubeConfig == null) {
            throw new MapleClusterConfigException("K8s 集群配置错误, name: " + name);
        }
        if ("file".equals(kubeConfig.getType())) {
            if (StringUtils.isBlank(kubeConfig.getKubeConfigContent())) {
                String kubeConfigFile = kubeConfig.getKubeConfigFile();
                if (StringUtils.isBlank(kubeConfigFile)) {
                    throw new MapleClusterConfigException("K8s 配置文件路径不能为空, name: " + name);
                }
                Path path = Paths.get(kubeConfigFile);
                if (Files.notExists(path)) {
                    throw new MapleClusterConfigException("K8s 配置文件不存在, name: " + name);
                }
                try {
                    return Config.fromKubeconfig(Files.readString(path));
                } catch (IOException e) {
                    throw new MapleClusterConfigException("K8s 配置文件读取失败, name: " + name, e);
                }
            }
            return Config.fromKubeconfig(kubeConfig.getKubeConfigContent());
        }
        Config config = kubeConfig.getConfig();
        config.setMasterUrl(master);
        return config;
    }
}
