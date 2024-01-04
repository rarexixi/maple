package org.xi.maple.executor.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.exception.MapleClusterNotSupportException;
import org.xi.maple.executor.builder.EngineExecutor;
import org.xi.maple.executor.builder.strategy.K8SEngineExecutor;
import org.xi.maple.executor.builder.strategy.YarnEngineExecutor;
import org.xi.maple.executor.service.EngineExecutionService;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.util.HashMap;
import java.util.Map;

@Primary
@Service
public class EngineExecutionServiceImpl implements EngineExecutionService {

    final Map<String, EngineExecutor> engineExecutorMap;

    public EngineExecutionServiceImpl(K8SEngineExecutor k8sEngineBuilder, YarnEngineExecutor yarnEngineBuilder) {
        engineExecutorMap = new HashMap<>();
        engineExecutorMap.put(ClusterCategoryConstants.K8s, k8sEngineBuilder);
        engineExecutorMap.put(ClusterCategoryConstants.YARN, yarnEngineBuilder);
    }

    @Override
    public void execute(EngineExecutionDetailResponse execution) {
        getEngineBuilder(execution.getClusterCategory()).execute(execution);
    }

    private EngineExecutor getEngineBuilder(String clusterCategory) {
        if (!engineExecutorMap.containsKey(clusterCategory)) {
            throw new MapleClusterNotSupportException("不支持的集群类型");
        }
        return engineExecutorMap.get(clusterCategory);
    }
}
