package org.xi.maple.manager.k8s.crds.volcano;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.fabric8.kubernetes.api.model.IntOrString;
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
public class VolcanoQueueSpec implements Serializable {

    private Map<String, Object> affinity;
    private Map<String, IntOrString> capability;
    private Map<String, Object> extendClusters;
    private Map<String, Object> guarantee;
    private Boolean reclaimable;
    private String type;
    private Integer weight;
}
