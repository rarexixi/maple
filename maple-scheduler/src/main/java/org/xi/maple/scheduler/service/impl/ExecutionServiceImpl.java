package org.xi.maple.scheduler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.scheduler.client.ExecutionManagerClient;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;
import org.xi.maple.scheduler.k8s.service.K8sClusterService;
import org.xi.maple.scheduler.model.ClusterQueue;
import org.xi.maple.scheduler.service.ExecutionService;
import org.xi.maple.scheduler.yarn.service.YarnClusterService;

import java.util.List;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    final PersistenceClient persistenceClient;

    final ExecutionManagerClient executionManagerClient;

    final YarnClusterService yarnClusterService;

    final K8sClusterService k8sClusterService;

    private final UpdateExecStatusFunc updateExecStatusFunc;

    public ExecutionServiceImpl(PersistenceClient persistenceClient, ExecutionManagerClient executionManagerClient, YarnClusterService yarnClusterService, K8sClusterService k8sClusterService, UpdateExecStatusFunc updateExecStatusFunc) {
        this.persistenceClient = persistenceClient;
        this.executionManagerClient = executionManagerClient;
        this.yarnClusterService = yarnClusterService;
        this.k8sClusterService = k8sClusterService;
        this.updateExecStatusFunc = updateExecStatusFunc;
    }

    @Override
    public List<EngineExecutionQueue> getExecQueueList(EngineExecutionQueueQueryRequest request) {
        return persistenceClient.getExecQueueList(request);
    }

    @Override
    public EngineExecutionDetailResponse getExecutionById(int execId) {
        return persistenceClient.getExecutionById(execId);
    }

    @Override
    public void updateExecutionStatus(int execId, String status) {
        updateExecStatusFunc.apply(execId, status);
    }

    @Override
    public void execute(EngineExecutionDetailResponse execution) {
        executionManagerClient.execute(execution);
    }

    @Override
    public void submitExecution(int execId) {
        EngineExecutionDetailResponse execution = persistenceClient.getExecutionById(execId);
        if (execution == null) {
            logger.error("作业不存在，id: {}", execId);
            return;
        }
        submitExecution(execution, () -> {
            logger.warn("队列没有足够的资源，cluster: {}, queue: {}", execution.getCluster(), execution.getClusterQueue());
            updateExecStatusFunc.apply(execution.getId(), EngineExecutionStatus.STARTED_FAILED);
        });
    }

    @Override
    public void submitExecution(EngineExecutionDetailResponse execution, Runnable queueBusyCallback) {
        ClusterQueue cachedQueueInfo = null;
        if (ClusterCategoryConstants.K8s.equals(execution.getClusterCategory())) {
            cachedQueueInfo = k8sClusterService.getCachedQueueInfo(execution.getCluster(), execution.getClusterQueue());
        } else if (ClusterCategoryConstants.YARN.equals(execution.getClusterCategory())) {
            cachedQueueInfo = yarnClusterService.getCachedQueueInfo(execution.getCluster(), execution.getClusterQueue());
        } else {
            logger.error("不支持的集群类型，cluster: {}, queue: {}", execution.getCluster(), execution.getClusterQueue());
        }
        // 单次任务需要新建引擎，判断队列是否有排队任务，有排队任务说明资源不足，直接返回
        if (cachedQueueInfo == null) {
            logger.error("队列不存在，cluster: {}, queue: {}", execution.getCluster(), execution.getClusterQueue());
            // 修改作业状态
            updateExecStatusFunc.apply(execution.getId(), EngineExecutionStatus.STARTED_FAILED);
        } else if (!cachedQueueInfo.idle()) {
            queueBusyCallback.run();
        } else {
            logger.info("submit execution: {}", execution);
            executionManagerClient.execute(execution);
        }
    }
}
