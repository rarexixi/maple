package org.xi.maple.execution.k8s.flink.crds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkDeploymentStatus implements Serializable {
    private Map<String, String> clusterInfo;
    private String jobManagerDeploymentStatus;
    private Map<String, Object> reconciliationStatus;
    private Map<String, Object> jobStatus;
    private String error;
    private String lifecycleState;
    private Map<String, Object> taskManager;
}
