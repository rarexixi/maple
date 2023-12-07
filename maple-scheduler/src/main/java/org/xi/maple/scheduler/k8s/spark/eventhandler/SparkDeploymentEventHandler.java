package org.xi.maple.scheduler.k8s.spark.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.scheduler.k8s.BaseResourceEventHandler;
import org.xi.maple.scheduler.k8s.spark.crds.SparkDeployment;
import org.xi.maple.scheduler.k8s.spark.crds.SparkDeploymentSpec;
import org.xi.maple.scheduler.k8s.spark.crds.SparkDeploymentStatus;

import java.util.Map;
import java.util.function.BiFunction;

public class SparkDeploymentEventHandler extends BaseResourceEventHandler<SparkDeploymentSpec, SparkDeploymentStatus, SparkDeployment> {

    private static final Logger logger = LoggerFactory.getLogger(SparkDeploymentEventHandler.class);

    public SparkDeploymentEventHandler(BiFunction<Integer, String, Integer> updateFunc) {
        super(updateFunc);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getType() {
        return "spark";
    }

    @Override
    public String getState(SparkDeployment obj) {
        if (obj.getStatus() == null || obj.getStatus().getApplicationState() == null) {
            return null;
        }
        Map<String, Object> applicationState = obj.getStatus().getApplicationState();
        if (applicationState.containsKey("state")) {
            return String.valueOf(applicationState.get("state"));
        }
        return null;
    }
}