package org.xi.maple.manager.k8s.spark.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.manager.k8s.BaseResourceEventHandler;
import org.xi.maple.manager.k8s.spark.crds.SparkApplication;
import org.xi.maple.manager.k8s.spark.crds.SparkApplicationSpec;
import org.xi.maple.manager.k8s.spark.crds.SparkApplicationStatus;

import java.util.Map;
import java.util.function.BiFunction;

public class SparkApplicationEventHandler extends BaseResourceEventHandler<SparkApplicationSpec, SparkApplicationStatus, SparkApplication> {

    private static final Logger logger = LoggerFactory.getLogger(SparkApplicationEventHandler.class);

    public SparkApplicationEventHandler(BiFunction<Integer, EngineExecutionUpdateStatusRequest, Integer> updateFunc) {
        super(logger, updateFunc, EngineCategoryConstants.SPARK);
    }

    @Override
    public EngineExecutionUpdateStatusRequest getState(SparkApplication obj) {
        Map<String, Object> applicationState;
        if (obj.getStatus() != null && (applicationState = obj.getStatus().getApplicationState()) != null && applicationState.containsKey("state")) {
            String raw_status = String.valueOf(applicationState.get("state"));
            String status = ""; // todo
            return new EngineExecutionUpdateStatusRequest(status, raw_status);
        }
        return null;
    }
}