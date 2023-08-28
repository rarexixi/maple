package org.xi.maple.k8s.spark.crds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkDeploymentSpec implements Serializable {
    private List<String> arguments;
    private String batchScheduler;
    private Map<String, Object> batchSchedulerOptions;
    private Map<String, Object> deps;
    private Map<String, Object> driver;
    private Map<String, Object> dynamicAllocation;
    private Map<String, Object> executor;
    private Integer failureRetries;
    private Map<String, String> hadoopConf;
    private String hadoopConfigMap;
    private String image;
    private String imagePullPolicy;
    private List<String> imagePullSecrets;
    private String mainApplicationFile;
    private String mainClass;
    private String memoryOverheadFactor;
    private String mode;
    private Map<String, Object> monitoring;
    private Map<String, String> nodeSelector;
    private String proxyUser;
    private String pythonVersion;
    private Map<String, Object> restartPolicy;
    private Long retryInterval;
    private Map<String, String> sparkConf;
    private String sparkConfigMap;
    private Map<String, Object> sparkUIOptions;
    private String sparkVersion;
    private Long timeToLiveSeconds;
    private String type;
    private List<Object> volumes;
}
