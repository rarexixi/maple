package org.xi.maple.execution.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.execution.client.fallback.SchedulerClientFallbackFactory;

import java.util.List;
import java.util.Map;

@FeignClient(value = MapleServiceName.SCHEDULER_SERVICE, fallbackFactory = SchedulerClientFallbackFactory.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("/k8s/{clusterName}/deploy")
    List<Map<String, ?>> deploy(@PathVariable("clusterName") String clusterName, @RequestBody String yaml);

    // endregion
}
