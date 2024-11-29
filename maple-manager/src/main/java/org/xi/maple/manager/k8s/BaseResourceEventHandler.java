package org.xi.maple.manager.k8s;

import io.fabric8.kubernetes.client.CustomResource;
import org.slf4j.Logger;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.manager.constant.MapleConstants;

import java.util.function.BiFunction;

public abstract class BaseResourceEventHandler<S, T, A extends CustomResource<S, T>> implements MapleResourceEventHandler<A> {

    protected final Logger logger;
    protected final BiFunction<Integer, EngineExecutionStatusUpdateReq, Integer> updateFunc;
    protected final String type;

    public BaseResourceEventHandler(Logger logger, BiFunction<Integer, EngineExecutionStatusUpdateReq, Integer> updateFunc, String type) {
        this.logger = logger;
        this.updateFunc = updateFunc;
        this.type = type;
    }

    @Override
    public void onAdd(A obj) {
        int execId = getExecId(obj);
        String mapleAppName = getAppName(obj);
        EngineExecutionStatusUpdateReq state = getState(obj);
        updateFunc.apply(execId, state);

        logger.info("{}[{}] from {} change state to {} in action[{}]", type, execId, mapleAppName, state, "ADD");
    }

    @Override
    public void onUpdate(A oldObj, A newObj) {
        int execId = getExecId(newObj);
        String mapleAppName = getAppName(newObj);
        EngineExecutionStatusUpdateReq state = getState(newObj);
        updateFunc.apply(execId, state);

        logger.info("{}[{}] from {} change state to {} in action[{}]", type, execId, mapleAppName, state, "UPDATE");
    }

    @Override
    public void onDelete(A obj, boolean deletedFinalStateUnknown) {
        int execId = getExecId(obj);
        String mapleAppName = getAppName(obj);
        EngineExecutionStatusUpdateReq state = getState(obj);
        updateFunc.apply(execId, state);

        logger.info("{}[{}] from {} change state to {} in action[{}]", type, execId, mapleAppName, state, "DELETE");
    }

    protected int getExecId(A obj) {
        String labelExecId = obj.getMetadata().getLabels().getOrDefault(MapleConstants.LABEL_ID, "0");
        return labelExecId == null ? 0 : Integer.parseInt(labelExecId);
    }

    protected String getAppName(A obj) {
        return obj.getMetadata().getLabels().getOrDefault(MapleConstants.LABEL_APP_NAME, "");
    }

    public abstract EngineExecutionStatusUpdateReq getState(A obj);
}
