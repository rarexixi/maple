package org.xi.maple.scheduler.k8s.volcano.crds;

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
public class VolcanoQueueStatus implements Serializable {
    private Map<String, IntOrString> allocated;
    private Integer completed;
    private Integer inqueue;
    private Integer pending;
    private Map<String, Object> reservation;
    private Integer running;
    private String state;
    private Integer unknown;
}
