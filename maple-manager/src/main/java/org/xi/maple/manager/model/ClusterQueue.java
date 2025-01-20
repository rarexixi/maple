package org.xi.maple.manager.model;

/**
 * @author xishihao
 */
public interface ClusterQueue {

    boolean idle();

    static String getClusterQueueKey(Integer clusterId, String queueName) {
        return clusterId + "->" + queueName;
    }
}
