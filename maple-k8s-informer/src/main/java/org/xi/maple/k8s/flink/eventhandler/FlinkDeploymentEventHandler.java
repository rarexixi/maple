package org.xi.maple.k8s.flink.eventhandler;

import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import org.xi.maple.k8s.flink.crds.FlinkDeployment;

public class FlinkDeploymentEventHandler implements ResourceEventHandler<FlinkDeployment> {
    @Override
    public void onNothing() {
        ResourceEventHandler.super.onNothing();
    }

    @Override
    public void onAdd(FlinkDeployment obj) {

    }

    @Override
    public void onUpdate(FlinkDeployment oldObj, FlinkDeployment newObj) {

    }

    @Override
    public void onDelete(FlinkDeployment obj, boolean deletedFinalStateUnknown) {

    }
}