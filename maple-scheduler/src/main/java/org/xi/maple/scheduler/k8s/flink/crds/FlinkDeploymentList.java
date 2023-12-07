package org.xi.maple.scheduler.k8s.flink.crds;

import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Singular;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("flink.apache.org")
@Version("v1beta1")
@Singular("flinkdeployment")
@Plural("flinkdeployments")
public class FlinkDeploymentList extends DefaultKubernetesResourceList<FlinkDeployment> {
}
