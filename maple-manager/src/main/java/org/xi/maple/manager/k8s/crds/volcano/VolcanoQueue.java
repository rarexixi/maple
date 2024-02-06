package org.xi.maple.manager.k8s.crds.volcano;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize()
@Group("scheduling.volcano.sh")
@Version("v1beta1")
@Kind("Queue")
@Singular("queue")
@Plural("queues")
public class VolcanoQueue extends CustomResource<VolcanoQueueSpec, VolcanoQueueStatus> implements Namespaced {
}
