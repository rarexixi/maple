package org.xi.maple.scheduler.service.impl;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterTypeConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.redis.model.MapleClusterQueue;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueue;
import org.xi.maple.scheduler.k8s.volcano.crds.VolcanoQueueList;
import org.xi.maple.scheduler.model.YarnCluster;
import org.xi.maple.scheduler.service.ClusterQueueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xishihao
 */
@Service
public class ClusterQueueServiceImpl implements ClusterQueueService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterQueueServiceImpl.class);

    private final PersistenceClient client;

    final static Map<String, MapleClusterQueue> CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();
    final static Map<String, SharedIndexInformer<VolcanoQueue>> k8sQueueInformer = new ConcurrentHashMap<>();

    /**
     * 集群名称 -> KubernetesClient
     */
    private final Map<String, KubernetesClient> k8sClients;

    public ClusterQueueServiceImpl(PersistenceClient client) {
        this.client = client;
        this.k8sClients = new HashMap<>();
    }

    @Override
    public void cacheQueueInfos(Map<String, MapleClusterQueue> queues) {
        CLUSTER_QUEUE_MAP.putAll(queues);
    }

    @Override
    public void cacheQueueInfo(String clusterName, String queue, MapleClusterQueue queueInfo) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        CLUSTER_QUEUE_MAP.put(key, queueInfo);
    }

    @Override
    public MapleClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        return CLUSTER_QUEUE_MAP.get(key);
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
                logger.error("close kubernetes client error, clusterName: " + clusterName, t);
            }
        }
        KubernetesClient kubernetesClient = getKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
        k8sClients.put(cluster.getName(), kubernetesClient);
        refreshExecStatus(clusterName, kubernetesClient);
    }

    /**
     * 刷新引擎执行任务状态
     */
    @Scheduled
    public void refreshClusters() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setCategory(ClusterTypeConstants.K8s);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            KubernetesClient kubernetesClient;
            if (k8sClients.containsKey(cluster.getName())) {
                kubernetesClient = k8sClients.get(cluster.getName());
            } else {
                kubernetesClient = getKubernetesClient(cluster.getAddress(), cluster.getConfiguration());
                k8sClients.put(cluster.getName(), kubernetesClient);
            }
            refreshExecStatus(cluster.getName(), kubernetesClient);
        }
    }

    /**
     * 刷新引擎执行任务状态
     *
     * @param kubernetesClient
     */
    public void refreshExecStatus(String clusterName, KubernetesClient kubernetesClient) {

        SharedIndexInformer<VolcanoQueue> volcanoInformer = kubernetesClient
                .resources(VolcanoQueue.class, VolcanoQueueList.class)
                .inform(new ResourceEventHandler<>() {
                    @Override
                    public void onAdd(VolcanoQueue volcanoQueue) {

                    }

                    @Override
                    public void onUpdate(VolcanoQueue volcanoQueue, VolcanoQueue newVolcanoQueue) {

                    }

                    @Override
                    public void onDelete(VolcanoQueue volcanoQueue, boolean deletedFinalStateUnknown) {
                        String key = MapleClusterQueue.getKey(clusterName, volcanoQueue.getMetadata().getName());
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
    private KubernetesClient getKubernetesClient(String master, String configJson) {
        KubeConfigWithType kubeConfig = JsonUtils.parseObject(configJson, KubeConfigWithType.class, null);
        if (kubeConfig == null) {
            Config config = new ConfigBuilder().withMasterUrl(master).build();
            return new KubernetesClientBuilder().withConfig(config).build();
        }
        if ("file".equals(kubeConfig.getType())) {
            if (StringUtils.isNotBlank(kubeConfig.getKubeConfigFile())) {
                Config config = Config.fromKubeconfig(kubeConfig.getKubeConfigFile());
                config.setMasterUrl(master);
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
