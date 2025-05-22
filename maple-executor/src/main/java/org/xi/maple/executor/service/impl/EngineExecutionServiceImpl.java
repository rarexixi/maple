package org.xi.maple.executor.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.exception.MapleClusterNotSupportException;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.executor.builder.EngineExecutor;
import org.xi.maple.executor.builder.strategy.K8SEngineExecutor;
import org.xi.maple.executor.builder.strategy.YarnEngineExecutor;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.executor.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.response.EngineExecutionAction;

import java.util.HashMap;
import java.util.Map;

@Primary
@Service
public class EngineExecutionServiceImpl implements EngineExecutionService {

    protected final PersistenceClient persistenceClient;
    final Map<String, EngineExecutor> engineExecutorMap;

    public EngineExecutionServiceImpl( PersistenceClient persistenceClient, K8SEngineExecutor k8sEngineBuilder, YarnEngineExecutor yarnEngineBuilder) {
        this.persistenceClient = persistenceClient;
        engineExecutorMap = new HashMap<>();
        engineExecutorMap.put(ClusterCategoryConstants.K8s, k8sEngineBuilder);
        engineExecutorMap.put(ClusterCategoryConstants.YARN, yarnEngineBuilder);
    }

    @Override
    public void execute(EngineExecutionAction execution) throws Exception {
        getEngineBuilder(execution.getClusterCategory()).execute(convert(execution));
    }

    @Override
    public void operate(EngineExecutionAction execution) throws Exception {
        getEngineBuilder(execution.getClusterCategory()).operate(convert(execution));
    }

    private EngineExecutor getEngineBuilder(String clusterCategory) {
        if (!engineExecutorMap.containsKey(clusterCategory)) {
            throw new MapleClusterNotSupportException("不支持的集群类型");
        }
        return engineExecutorMap.get(clusterCategory);
    }

    protected EngineExecutionModel convert(EngineExecutionAction execution) {
        ClusterEngineDefaultConfGetRequest request = new ClusterEngineDefaultConfGetRequest(execution.getUserGroup(), execution.getRunBy());
        EngineConf engineConf = persistenceClient.getEngineConf(execution.getEngineId(), request);

        return new EngineExecutionModel()
                .withAction(execution.getAction())
                .withParams(execution.getParams())

                .withClusterId(execution.getClusterId())
                .withClusterCategory(execution.getClusterCategory())
                .withEngineCategory(execution.getEngineCategory())
                .withEngineVersion(execution.getEngineVersion())
                .withEngine(engineConf)

                .withExecId(execution.getId())
                .withExecFile(execution.getExecFile())
                .withJobId(execution.getJobId())
                .withFromApp(execution.getFromApp())
                .withBizId(execution.getBizId())
                .withExecName(execution.getExecName())
                .withJobType(execution.getJobType())
                .withResourceGroup(execution.getResourceGroup())
                .withUserGroup(execution.getUserGroupName())
                .withRunBy(execution.getRunByName())

                .withClusterAppId(execution.getClusterAppId())
                .withClusterAppAddress(execution.getClusterAppAddress())

                .withRunConf(execution.getRunConf())
                .withExecConf(execution.getExecConf())
                .withExecInfo(execution.getExecInfo());
    }
}
