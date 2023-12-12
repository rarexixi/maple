package org.xi.maple.scheduler.k8s.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.scheduler.model.ClusterQueue;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class K8sClusterQueue implements ClusterQueue, Serializable {

    private Integer pending = 0;

    @Override
    public boolean idle() {
        return pending != null && pending == 0;
    }
}
