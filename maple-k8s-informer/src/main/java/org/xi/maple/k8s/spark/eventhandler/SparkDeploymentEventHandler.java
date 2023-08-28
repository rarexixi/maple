package org.xi.maple.k8s.spark.eventhandler;

import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import org.xi.maple.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.k8s.spark.crds.SparkDeployment;

public class SparkDeploymentEventHandler implements ResourceEventHandler<SparkDeployment> {
    @Override
    public void onNothing() {
        ResourceEventHandler.super.onNothing();
    }

    @Override
    public void onAdd(SparkDeployment obj) {

    }

    @Override
    public void onUpdate(SparkDeployment oldObj, SparkDeployment newObj) {

    }

    @Override
    public void onDelete(SparkDeployment obj, boolean deletedFinalStateUnknown) {

    }
}