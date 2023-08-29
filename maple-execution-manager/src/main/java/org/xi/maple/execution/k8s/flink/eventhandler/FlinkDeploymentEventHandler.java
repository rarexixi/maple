package org.xi.maple.execution.k8s.flink.eventhandler;

import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeployment;

public class FlinkDeploymentEventHandler implements ResourceEventHandler<FlinkDeployment> {
    @Override
    public void onNothing() {
        ResourceEventHandler.super.onNothing();
    }

    @Override
    public void onAdd(FlinkDeployment obj) {
        String execId = obj.getMetadata().getLabels().getOrDefault("maple-id", "0");

        System.out.println("onAdd: " + obj.getMetadata().getName());
    }

    @Override
    public void onUpdate(FlinkDeployment oldObj, FlinkDeployment newObj) {
        String execId = oldObj.getMetadata().getLabels().getOrDefault("maple-id", "0");
        String state = String.valueOf(newObj.getStatus().getJobStatus().get("state"));

        System.out.println("onUpdate: " + newObj.getMetadata().getName());
    }

    @Override
    public void onDelete(FlinkDeployment obj, boolean deletedFinalStateUnknown) {
        System.out.println("onDelete: " + obj.getMetadata().getName());
    }
}