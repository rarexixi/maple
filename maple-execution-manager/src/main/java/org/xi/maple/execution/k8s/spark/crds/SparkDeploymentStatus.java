package org.xi.maple.execution.k8s.spark.crds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkDeploymentStatus {
    private Map<String, Object> applicationState;
    private Map<String, Object> driverInfo;
    private Integer executionAttempts;
    private Map<String, String> executorState;
    private String lastSubmissionAttemptTime;
    private String sparkApplicationId;
    private Integer submissionAttempts;
    private String submissionID;
    private String terminationTime;
}
