package org.xi.maple.scheduler.k8s.spark.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.scheduler.k8s.BaseResourceEventHandler;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplication;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationSpec;
import org.xi.maple.scheduler.k8s.spark.crds.SparkApplicationStatus;

import java.util.Map;
import java.util.function.BiFunction;

public class SparkApplicationEventHandler extends BaseResourceEventHandler<SparkApplicationSpec, SparkApplicationStatus, SparkApplication> {

    private static final Logger logger = LoggerFactory.getLogger(SparkApplicationEventHandler.class);

    public SparkApplicationEventHandler(BiFunction<Integer, String, Integer> updateFunc) {
        super(logger, updateFunc, EngineCategoryConstants.SPARK);
    }

    @Override
    public String getState(SparkApplication obj) {
        Map<String, Object> applicationState;
        if (obj.getStatus() != null && (applicationState = obj.getStatus().getApplicationState()) != null && applicationState.containsKey("state")) {
            return String.valueOf(applicationState.get("state"));
        }
        return null;
    }
}