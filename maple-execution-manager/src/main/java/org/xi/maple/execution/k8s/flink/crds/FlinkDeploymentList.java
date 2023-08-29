package org.xi.maple.execution.k8s.flink.crds;

import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList;
import io.fabric8.kubernetes.model.annotation.*;

@Group("flink.apache.org")
@Version("v1beta1")
@Singular("flinkdeployment")
@Plural("flinkdeployments")
public class FlinkDeploymentList extends DefaultKubernetesResourceList<FlinkDeployment> {
}
