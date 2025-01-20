package org.xi.maple.manager.yarn.service;

import org.xi.maple.manager.service.ClusterService;

/**
 * @author xishihao
 */
public interface YarnClusterService extends ClusterService {
    Object kill(Integer clusterId, String applicationId);

    void refreshExecutionStatus(Integer clusterId, String applicationId);

    void refreshExecutionsStatus(Integer clusterId, String states, Long startedTimeBegin, Long startedTimeEnd);

    void startRefreshScheduler();

    void stopRefreshScheduler();
}
