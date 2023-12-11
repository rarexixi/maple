package org.xi.maple.scheduler.k8s.spark.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.scheduler.k8s.BaseResourceEventHandler;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplication;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationSpec;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationStatus;

import java.util.Map;
import java.util.function.BiFunction;

public class SparkApplicationEventHandler extends BaseResourceEventHandler<SparkApplicationSpec, SparkApplicationStatus, SparkApplication> {

    private static final Logger logger = LoggerFactory.getLogger(SparkApplicationEventHandler.class);

    public SparkApplicationEventHandler(BiFunction<Integer, String, Integer> updateFunc) {
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
    public String getState(SparkApplication obj) {
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