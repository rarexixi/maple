package org.xi.maple.manager.service;

import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.model.response.EngineExecutionQueueResp;

import java.util.List;
import java.util.Map;

public interface ExecutionService {

    void submitToCluster(int execId);

    void submitToCluster(EngineExecutionDetailResp execution, Runnable queueBusyCallback);

    Object kill(Integer id, String app);

    void operate(Integer id, String action, String app, Map<String, ?> params);

    List<EngineExecutionQueueResp> getExecQueueList();

    EngineExecutionDetailResp getExecutionById(int execId);

    void updateExecutionStatus(int execId, EngineExecutionStatusUpdateReq statusRequest);

    void refreshCluster(ClusterMessage clusterMessage);
}
