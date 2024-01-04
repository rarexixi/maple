package org.xi.maple.manager.k8s.volcano.crds;

import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.model.annotation.*;

@Group("scheduling.volcano.sh")
@Version("v1beta1")
@Singular("queue")
@Plural("queues")
public class VolcanoQueueList extends DefaultKubernetesResourceList<VolcanoQueue> implements Namespaced {
}
