package org.xi.maple.datacalc.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.maple.datacalc.api.client.fallback.SchedulerClientFallbackFactory;

@FeignClient(value = "maple-scheduler", fallbackFactory = SchedulerClientFallbackFactory.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("exec-now")
    void submitExecution(@RequestParam("execId") int execId);

    // endregion
}
