package org.xi.maple.scheduler.service.impl;

import org.springframework.stereotype.Service;
import org.xi.maple.scheduler.model.MapleClusterQueue;
import org.xi.maple.scheduler.service.ClusterQueueService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClusterQueueServiceImpl implements ClusterQueueService {

    final static Map<String, MapleClusterQueue> CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();

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
    public void deleteCacheQueueInfo(String clusterName, String queue) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        CLUSTER_QUEUE_MAP.remove(key);
    }

    @Override
    public MapleClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        return CLUSTER_QUEUE_MAP.get(key);
    }
}
