package org.xi.maple.execution.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.execution.client.fallback.PersistenceClientFallbackFactory;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;

@FeignClient(value = "maple-persistence-service", fallbackFactory = PersistenceClientFallbackFactory.class)
public interface PersistenceClient {

    // region engine-execution

    @PatchMapping("/engine-execution/update-status")
    Integer updateExecutionStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionExtInfoById(@RequestBody EngineExecutionUpdateRequest updateRequest);

    // endregion
}
