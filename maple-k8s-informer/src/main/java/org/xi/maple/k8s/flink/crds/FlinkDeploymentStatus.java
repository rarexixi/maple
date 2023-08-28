package org.xi.maple.k8s.flink.crds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashMap;
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
    private Object jobManagerDeploymentStatus;
    private Object reconciliationStatus;
    private Object jobStatus;
    private String error;
    private String lifecycleState;
    private Object taskManager;
}
