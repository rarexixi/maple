package org.xi.maple.manager.yarn.service;

import org.xi.maple.manager.service.ClusterService;

/**
 * @author xishihao
 */
public interface YarnClusterService extends ClusterService {
    Object kill(String clusterName, String applicationId);

    void refreshExecutionStatus(String clusterName, String applicationId);

    void refreshExecutionsStatus(String clusterName, String states, Long startedTimeBegin, Long startedTimeEnd);

    void startRefreshScheduler();

    void stopRefreshScheduler();
}
