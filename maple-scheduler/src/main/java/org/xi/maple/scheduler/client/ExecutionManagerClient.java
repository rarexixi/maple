package org.xi.maple.scheduler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.scheduler.client.fallback.ExecutionManagerClientFallbackFactory;

@FeignClient(value = "maple-execution-manager", fallbackFactory = ExecutionManagerClientFallbackFactory.class)
public interface ExecutionManagerClient {

    // region engine-execution

    @PostMapping("/engine-execution/execute")
    void execute(EngineExecutionDetailResponse execution);

    // endregion
}