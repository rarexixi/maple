package org.xi.maple.scheduler.service;

import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.ClusterMessage;

import java.util.List;

public interface ExecutionService {

    List<EngineExecutionQueue> getExecQueueList(EngineExecutionQueueQueryRequest request);

    EngineExecutionDetailResponse getExecutionById(int execId);

    void updateExecutionStatus(int execId, String status);

    void execute(EngineExecutionDetailResponse execution);

    void submitExecution(int execId);

    void submitExecution(EngineExecutionDetailResponse execution, Runnable queueBusyCallback);

    void refreshCluster(ClusterMessage clusterMessage);
}
