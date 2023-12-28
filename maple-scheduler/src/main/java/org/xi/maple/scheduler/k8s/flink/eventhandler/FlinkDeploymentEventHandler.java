package org.xi.maple.scheduler.k8s.flink.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.scheduler.k8s.BaseResourceEventHandler;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeploymentSpec;
import org.xi.maple.scheduler.k8s.flink.crds.FlinkDeploymentStatus;

import java.util.Map;
import java.util.function.BiFunction;

public class FlinkDeploymentEventHandler extends BaseResourceEventHandler<FlinkDeploymentSpec, FlinkDeploymentStatus, FlinkDeployment> {

    private static final Logger logger = LoggerFactory.getLogger(FlinkDeploymentEventHandler.class);

    public FlinkDeploymentEventHandler(BiFunction<Integer, EngineExecutionUpdateStatusRequest, Integer> updateFunc) {
        super(logger, updateFunc, EngineCategoryConstants.FLINK);
    }

    @Override
    public EngineExecutionUpdateStatusRequest getState(FlinkDeployment obj) {
        Map<String, Object> jobStatus;
        if (obj.getStatus() != null && (jobStatus = obj.getStatus().getJobStatus()) != null && jobStatus.containsKey("state")) {

            String raw_status = String.valueOf(jobStatus.get("state"));
            String status = ""; // todo
            return new EngineExecutionUpdateStatusRequest(status, raw_status);
        }
        return null;
    }
}