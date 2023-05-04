package org.xi.maple.scheduler.service;

import org.xi.maple.redis.model.MapleClusterQueue;

import java.util.Map;

/**
 * @author xishihao
 */
public interface ClusterQueueService {

    void cacheQueueInfos(Map<String, MapleClusterQueue> queues);

    void cacheQueueInfo(String clusterName, String queue, MapleClusterQueue queueInfo);

    MapleClusterQueue getCachedQueueInfo(String clusterName, String queue);
}
