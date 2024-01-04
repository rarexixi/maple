package org.xi.maple.manager.k8s.spark.crds;

import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Singular;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1beta2")
@Group("sparkoperator.k8s.io")
@Singular("sparkapplication")
@Plural("sparkapplications")
public class SparkApplicationList extends DefaultKubernetesResourceList<SparkApplication> {

}