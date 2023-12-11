package org.xi.maple.execution.client;

import io.fabric8.kubernetes.api.model.HasMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.execution.client.fallback.PersistenceClientFallbackFactory;

import java.util.List;

@FeignClient(value = "maple-scheduler", fallbackFactory = PersistenceClientFallbackFactory.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("/k8s/{clusterName}/deploy")
    List<HasMetadata> deploy(@PathVariable("clusterName") String clusterName, @RequestBody String yaml);

    // endregion
}
