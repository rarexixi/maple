package org.xi.maple.manager.k8s.service.impl;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.exception.MapleClusterConfigException;
import org.xi.maple.common.exception.MapleClusterNotConfiguredException;
import org.xi.maple.common.exception.MapleEngineTypeNotSupportException;
import org.xi.maple.common.exception.MapleK8sException;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.manager.configuration.properties.MapleManagerProperties;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.manager.client.PersistenceClient;
import org.xi.maple.manager.constant.K8sResourceType;
import org.xi.maple.manager.constant.MapleConstants;
import org.xi.maple.manager.function.UpdateExecStatusFunc;
import org.xi.maple.manager.k8s.MapleResourceEventHandler;
import org.xi.maple.manager.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.manager.k8s.flink.crds.FlinkDeploymentList;
import org.xi.maple.manager.k8s.flink.eventhandler.FlinkDeploymentEventHandler;
import org.xi.maple.manager.k8s.model.K8sClusterQueue;
import org.xi.maple.manager.k8s.model.KubeConfigWithType;
import org.xi.maple.manager.k8s.service.K8sClusterService;
import org.xi.maple.manager.k8s.spark.crds.SparkApplication;
import org.xi.maple.manager.k8s.spark.crds.SparkApplicationList;
import org.xi.maple.manager.k8s.spark.eventhandler.SparkApplicationEventHandler;
import org.xi.maple.manager.k8s.volcano.crds.VolcanoQueue;
import org.xi.maple.manager.k8s.volcano.crds.VolcanoQueueList;
import org.xi.maple.manager.model.ClusterQueue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author xishihao
 */
@Service("k8sClusterService")
public class K8sClusterServiceImpl implements K8sClusterService, CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(K8sClusterServiceImpl.class);

    private final PersistenceClient client;

    private final UpdateExecStatusFunc updateExecStatusFunc;

    private final MapleManagerProperties managerProperties;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Map<String, ClusterQueue> CLUSTER_QUEUE_MAP;


    /**
     * 集群名称 -> KubernetesClient
     */
    private final Map<String, KubernetesClient> k8sClients;

    public K8sClusterServiceImpl(PersistenceClient client, UpdateExecStatusFunc updateExecStatusFunc, MapleManagerProperties managerProperties, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.client = client;
        this.updateExecStatusFunc = updateExecStatusFunc;
        this.managerProperties = managerProperties;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.k8sClients = new HashMap<>();
        CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();
    }

    // region engine operation

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

    // endregion

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
        KubernetesClient kubernetesClient = new KubernetesClientBuilder().withConfig(getConfig(cluster)).build();
        k8sClients.put(cluster.getName(), kubernetesClient);
        refreshExecStatus(cluster.getName(), kubernetesClient);
    }


    /**
     * 刷新 K8s 集群配置，不会重启已有集群，如果已有集群有变更，需要使用强制刷新
     */
    @Override
    public void refreshAllClusterConfig() {
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
                kubernetesClient = new KubernetesClientBuilder().withConfig(getConfig(cluster)).build();
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

    @Override
    public ClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        return CLUSTER_QUEUE_MAP.getOrDefault(ClusterQueue.getClusterQueueKey(clusterName, queue), null);
    }


    /**
     * 根据配置的 master 和 configJson 获取 Kubernetes 的 Config 类
     *
     * @param cluster 集群配置
     * @return Config
     */
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

    private ScheduledFuture<?> clusterConfigScheduledFuture = null;

    /**
     * 开启刷新集群配置（仅配置内容）
     */
    @Override
    public void startRefreshScheduler() {
        clusterConfigScheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(this::refreshAllClusterConfig, managerProperties.getK8sRefreshPeriod());
    }

    /**
     * 关闭刷新集群配置（仅配置内容）
     */
    @Override
    public void stopRefreshScheduler() {
        ActionUtils.cancelScheduledFuture(clusterConfigScheduledFuture);
    }

    @Override
    public void run(String... args) throws Exception {
        if (managerProperties.isRefreshK8s()) {
            startRefreshScheduler();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (KubernetesClient kubernetesClient : k8sClients.values()) {
                ActionUtils.executeQuietly(() -> stopRefreshScheduler());
                ActionUtils.executeQuietly(() -> kubernetesClient.informers().stopAllRegisteredInformers());
                ActionUtils.executeQuietly(kubernetesClient::close);
            }
        }));
    }
}
