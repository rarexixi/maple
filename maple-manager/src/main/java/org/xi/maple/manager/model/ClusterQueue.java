package org.xi.maple.manager.model;

/**
 * @author xishihao
 */
public interface ClusterQueue {

    boolean idle();

    static String getClusterQueueKey(String clusterName, String queueName) {
        return clusterName + "->" + queueName;
    }
}
