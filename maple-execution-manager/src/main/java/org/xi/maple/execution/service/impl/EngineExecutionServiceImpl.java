package org.xi.maple.execution.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterTypeConstants;
import org.xi.maple.common.exception.MapleClusterNotSupportException;
import org.xi.maple.execution.builder.EngineExecutor;
import org.xi.maple.execution.builder.strategy.K8SEngineExecutor;
import org.xi.maple.execution.builder.strategy.YarnEngineExecutor;
import org.xi.maple.execution.service.EngineExecutionService;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.util.HashMap;
import java.util.Map;

@Primary
@Service
public class EngineExecutionServiceImpl implements EngineExecutionService {

    final Map<String, EngineExecutor> engineExecutorMap;

    public EngineExecutionServiceImpl(K8SEngineExecutor k8sEngineBuilder, YarnEngineExecutor yarnEngineBuilder) {
        engineExecutorMap = new HashMap<>();
        engineExecutorMap.put(ClusterTypeConstants.K8s, k8sEngineBuilder);
        engineExecutorMap.put(ClusterTypeConstants.YARN, yarnEngineBuilder);
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
