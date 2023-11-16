package org.xi.maple.execution.k8s.flink.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.execution.k8s.BaseResourceEventHandler;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeploymentSpec;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeploymentStatus;

import java.util.Map;

public class FlinkDeploymentEventHandler extends BaseResourceEventHandler<FlinkDeploymentSpec, FlinkDeploymentStatus, FlinkDeployment> {

    private static final Logger logger = LoggerFactory.getLogger(FlinkDeploymentEventHandler.class);

    @Override
    protected String getType() {
        return "flink";
    }

    @Override
    public String getState(FlinkDeployment obj) {
        if (obj.getStatus() == null || obj.getStatus().getJobStatus() == null) {
            return null;
        }
        Map<String, Object> jobStatus = obj.getStatus().getJobStatus();
        if (jobStatus.containsKey("state")) {
            return String.valueOf(jobStatus.get("state"));
        }
        return null;
    }
}