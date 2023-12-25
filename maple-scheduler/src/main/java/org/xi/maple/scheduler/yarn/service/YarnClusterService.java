package org.xi.maple.scheduler.yarn.service;

import org.xi.maple.scheduler.service.ClusterService;

/**
 * @author xishihao
 */
public interface YarnClusterService extends ClusterService {
    Object kill(String clusterName, String applicationId);

    void refreshExecutionStatus(String clusterName, String applicationId);

    void refreshExecutionsStatus(String clusterName, String states, Long startedTimeBegin, Long startedTimeEnd);
}
