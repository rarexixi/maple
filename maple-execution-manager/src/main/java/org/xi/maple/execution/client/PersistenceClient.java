package org.xi.maple.execution.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.execution.client.fallback.PersistenceClientFallbackFactory;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "maple-persistence-service", fallbackFactory = PersistenceClientFallbackFactory.class)
public interface PersistenceClient {

    // region engine-execution

    @PatchMapping("/engine-execution/update-status")
    Integer updateExecutionStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionExtInfoById(@RequestBody EngineExecutionUpdateRequest updateRequest);

    // endregion
}
