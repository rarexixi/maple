package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.model.ClusterQueue;

public interface ClusterQueueService {

    ClusterQueue getCachedQueueInfo(String clusterName, String queue);
}
