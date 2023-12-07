package org.xi.maple.scheduler.k8s;

import io.fabric8.kubernetes.client.CustomResource;
import org.slf4j.Logger;

import java.util.function.BiFunction;

public abstract class BaseResourceEventHandler<S, T, A extends CustomResource<S, T>> implements MapleResourceEventHandler<A> {

    protected final BiFunction<Integer, String, Integer> updateFunc;

    public BaseResourceEventHandler(BiFunction<Integer, String, Integer> updateFunc) {
        this.updateFunc = updateFunc;
    }

    protected abstract Logger getLogger();

    @Override
    public void onAdd(A obj) {
        Logger logger = getLogger();
        int execId = getExecId(obj);
        String mapleAppName = getAppName(obj);
        String state = getState(obj);
        updateFunc.apply(execId, state);

        logger.info("{}[{}] from {} change state to {} in action[{}]", getType(), execId, mapleAppName, state, "ADD");
    }

    @Override
    public void onUpdate(A oldObj, A newObj) {
        Logger logger = getLogger();
        int execId = getExecId(newObj);
        String mapleAppName = getAppName(newObj);
        String state = getState(newObj);

        logger.info("{}[{}] from {} change state to {} in action[{}]", getType(), execId, mapleAppName, state, "UPDATE");
    }

    @Override
    public void onDelete(A obj, boolean deletedFinalStateUnknown) {
        Logger logger = getLogger();
        int execId = getExecId(obj);
        String mapleAppName = getAppName(obj);
        String state = getState(obj);

        logger.info("{}[{}] from {} change state to {} in action[{}]", getType(), execId, mapleAppName, state, "DELETE");
    }

    protected int getExecId(A obj) {
        String labelExecId = obj.getMetadata().getLabels().getOrDefault("maple-id", "0");
        return labelExecId == null ? 0 : Integer.parseInt(labelExecId);
    }

    protected String getAppName(A obj) {
        return obj.getMetadata().getLabels().getOrDefault("maple-app-name", "");
    }

    protected abstract String getType();

    public abstract String getState(A obj);
}
