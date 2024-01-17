package org.xi.maple.manager.service;

import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;

import java.util.List;
import java.util.Map;

public interface ExecutionService {

    void submitExecution(int execId);

    void submitExecution(EngineExecutionDetailResponse execution, Runnable queueBusyCallback);

    Object kill(Integer id);

    Object stop(Integer id, Map<String, ?> cancelParams);

    List<EngineExecutionQueue> getExecQueueList(EngineExecutionQueueQueryRequest request);

    EngineExecutionDetailResponse getExecutionById(int execId);

    void updateExecutionStatus(int execId, EngineExecutionUpdateStatusRequest statusRequest);

    void refreshCluster(ClusterMessage clusterMessage);
}
