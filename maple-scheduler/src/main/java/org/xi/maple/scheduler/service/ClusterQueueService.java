package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.model.MapleClusterQueue;

import java.util.Map;

public interface ClusterQueueService {

    void cacheQueueInfos(Map<String, MapleClusterQueue> queues);

    void cacheQueueInfo(String clusterName, String queue, MapleClusterQueue queueInfo);

    void deleteCacheQueueInfo(String clusterName, String queue);

    MapleClusterQueue getCachedQueueInfo(String clusterName, String queue);
}
