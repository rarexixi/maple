package org.xi.maple.execution.k8s.spark.crds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize()
@Group("sparkoperator.k8s.io")
@Version("v1beta2")
@Kind("SparkApplication")
@Singular("sparkapplication")
@Plural("sparkapplications")
public class SparkDeployment extends CustomResource<SparkDeploymentSpec, SparkDeploymentStatus> implements Namespaced {
}
