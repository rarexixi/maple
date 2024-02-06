package org.xi.maple.manager.k8s.crds.flink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.fabric8.kubernetes.api.model.Pod;
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
public class FlinkDeploymentSpec implements Serializable {
    private Map<String, Object> job;
    private Long restartNonce;
    private Map<String, String> flinkConfiguration;
    private String image;
    private String imagePullPolicy;
    private String serviceAccount;
    private String flinkVersion;
    private Map<String, Object> ingress;
    private Pod podTemplate;
    private Map<String, Object> jobManager;
    private Map<String, Object> taskManager;
    private Map<String, String> logConfiguration;
    private String mode;
}
