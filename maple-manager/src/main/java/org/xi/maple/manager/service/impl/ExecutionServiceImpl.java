package org.xi.maple.manager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleClusterNotSupportException;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.manager.client.ExecutorClient;
import org.xi.maple.manager.client.PersistenceClient;
import org.xi.maple.manager.function.UpdateExecStatusFunc;
import org.xi.maple.manager.k8s.service.K8sClusterService;
import org.xi.maple.manager.model.ClusterQueue;
import org.xi.maple.manager.service.ExecutionService;
import org.xi.maple.manager.yarn.service.YarnClusterService;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.model.response.EngineExecutionQueueResp;

import java.util.List;
import java.util.Map;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    final PersistenceClient persistenceClient;

    final ExecutorClient executorClient;

    final YarnClusterService yarnClusterService;

    final K8sClusterService k8sClusterService;

    private final UpdateExecStatusFunc updateExecStatusFunc;

    public ExecutionServiceImpl(PersistenceClient persistenceClient, ExecutorClient executorClient, YarnClusterService yarnClusterService, K8sClusterService k8sClusterService, UpdateExecStatusFunc updateExecStatusFunc) {
        this.persistenceClient = persistenceClient;
        this.executorClient = executorClient;
        this.yarnClusterService = yarnClusterService;
        this.k8sClusterService = k8sClusterService;
        this.updateExecStatusFunc = updateExecStatusFunc;
    }

    @Override
    public void submitExecution(int execId) {
        EngineExecutionDetailResp execution = persistenceClient.getExecutionById(execId);
        if (execution == null) {
            logger.error("作业不存在，id: {}", execId);
            return;
        }
        submitExecution(execution, () -> {
            logger.warn("队列资源不足，cluster: {}, queue: {}", execution.getClusterName(), execution.getResourceGroup());
            updateExecStatusFunc.apply(execution.getId(), new EngineExecutionStatusUpdateReq(EngineExecutionStatus.START_FAILED.toString(), "", 12, "队列资源不足"));
        });
    }

    @Override
    public void submitExecution(EngineExecutionDetailResp execution, Runnable queueBusyCallback) {
        ClusterQueue cachedQueueInfo;
        if (ClusterCategoryConstants.K8s.equalsIgnoreCase(execution.getClusterCategory())) {
            cachedQueueInfo = k8sClusterService.getCachedQueueInfo(execution.getClusterId(), execution.getResourceGroup());
        } else if (ClusterCategoryConstants.YARN.equalsIgnoreCase(execution.getClusterCategory())) {
            cachedQueueInfo = yarnClusterService.getCachedQueueInfo(execution.getClusterId(), execution.getResourceGroup());
        } else {
            logger.error("不支持的集群类型，cluster: {}, queue: {}", execution.getClusterName(), execution.getResourceGroup());
            throw new MapleClusterNotSupportException("不支持的集群类型，cluster: " + execution.getClusterName() + ", queue: " + execution.getResourceGroup());
        }
        // 单次任务需要新建引擎，判断队列是否有排队任务，有排队任务说明资源不足，直接返回
        if (cachedQueueInfo == null) {
            logger.error("队列不存在，cluster: {}, queue: {}", execution.getClusterName(), execution.getResourceGroup());
            // 修改作业状态
            updateExecStatusFunc.apply(execution.getId(), new EngineExecutionStatusUpdateReq(EngineExecutionStatus.START_FAILED.toString(), "", 12, "队列不存在"));
        } else if (!cachedQueueInfo.idle()) {
            queueBusyCallback.run();
        } else {
            logger.info("submit execution: {}", execution);
            try {
                executorClient.execute(execution);
            } catch (Throwable t) {
                logger.error("执行作业失败，id: {}", execution.getId(), t);
                updateExecStatusFunc.apply(execution.getId(), new EngineExecutionStatusUpdateReq(EngineExecutionStatus.START_FAILED.toString(), "", 12, "执行作业失败"));
            }
        }
    }

    @Override
    public Object kill(Integer id) {
        EngineExecutionDetailResp execution = getExecutionById(id);
        if (ClusterCategoryConstants.K8s.equals(execution.getClusterCategory())) {
            return null; // k8sClusterService.deleteEngine(execution.getClusterId(), execution.getNamespace(), execution.getEngineCategory(), execution.getClusterAppId());
        } else if (ClusterCategoryConstants.YARN.equals(execution.getClusterCategory())) {
            return yarnClusterService.kill(execution.getClusterId(), execution.getClusterAppId());
        } else {
            throw new MapleClusterNotSupportException("不支持的集群类型，id: " + id);
        }
    }

    @Override
    public Object stop(Integer id, Map<String, ?> cancelParams) {
        EngineExecutionDetailResp execution = getExecutionById(id);
        // execution.getConfiguration().putAll(cancelParams);
        // executionManagerClient.stop(execution);
        return null;
    }

    @Override
    public List<EngineExecutionQueueResp> getExecQueueList(EngineExecutionQueueQueryReq request) {
        return persistenceClient.getExecQueueList(request);
    }

    @Override
    public EngineExecutionDetailResp getExecutionById(int execId) {
        return persistenceClient.getExecutionById(execId);
    }

    @Override
    public void updateExecutionStatus(int execId, EngineExecutionStatusUpdateReq statusRequest) {
        updateExecStatusFunc.apply(execId, statusRequest);
    }

    @Override
    public void refreshCluster(ClusterMessage clusterMessage) {
        Integer clusterId = clusterMessage.getClusterId();
        ClusterDetailResp cluster = persistenceClient.getClusterById(clusterId);
        if (ClusterMessage.Type.DELETE == clusterMessage.getType()) {
            if (ClusterCategoryConstants.K8s.equals(cluster.getCategory())) {
                k8sClusterService.removeClusterConfig(clusterId);
            } else if (ClusterCategoryConstants.YARN.equals(cluster.getCategory())) {
                yarnClusterService.removeClusterConfig(clusterId);
            }
        } else {
            if (ClusterCategoryConstants.K8s.equals(cluster.getCategory())) {
                if (ClusterMessage.Type.UPDATE == clusterMessage.getType()) {
                    k8sClusterService.removeClusterConfig(clusterId);
                }
                k8sClusterService.addClusterConfig(cluster);
            } else if (ClusterCategoryConstants.YARN.equals(cluster.getCategory())) {
                if (ClusterMessage.Type.UPDATE == clusterMessage.getType()) {
                    yarnClusterService.removeClusterConfig(clusterId);
                }
                yarnClusterService.addClusterConfig(cluster);
            } else {
                logger.error("不支持的集群类型，cluster: {}, category: {}", cluster.getName(), cluster.getCategory());
            }
        }
    }
}
