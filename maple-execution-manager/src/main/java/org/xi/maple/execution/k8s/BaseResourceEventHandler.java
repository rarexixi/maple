package org.xi.maple.execution.k8s;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseResourceEventHandler<S, T, A extends CustomResource<S, T>> implements ResourceEventHandler<A> {

    private static final Logger logger = LoggerFactory.getLogger(BaseResourceEventHandler.class);

    @Override
    public void onNothing() {
        ResourceEventHandler.super.onNothing();
    }

    @Override
    public void onAdd(A obj) {
        int execId = getExecId(obj);
        String mapleAppName = getAppName(obj);
        String state = getState(obj);

        logger.info("{}[{}] from {} change state to {} in action[{}]", getType(), execId, mapleAppName, state, "ADD");
    }

    @Override
    public void onUpdate(A oldObj, A newObj) {
        int execId = getExecId(newObj);
        String mapleAppName = getAppName(newObj);
        String state = getState(newObj);

        logger.info("{}[{}] from {} change state to {} in action[{}]", getType(), execId, mapleAppName, state, "UPDATE");
    }

    @Override
    public void onDelete(A obj, boolean deletedFinalStateUnknown) {
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
