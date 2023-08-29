package org.xi.maple.execution.k8s.flink.crds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize()
@Group("flink.apache.org")
@Version("v1beta1")
@Kind("FlinkDeployment")
@Singular("flinkdeployment")
@Plural("flinkdeployments")
@ShortNames({"flinkdep"})
public class FlinkDeployment extends CustomResource<FlinkDeploymentSpec, FlinkDeploymentStatus> implements Namespaced {
}
