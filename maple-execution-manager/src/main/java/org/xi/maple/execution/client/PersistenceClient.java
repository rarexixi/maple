package org.xi.maple.execution.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.execution.client.fallback.PersistenceClientFallbackFactory;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;

@FeignClient(value = MapleServiceName.PERSISTENCE_SERVICE, fallbackFactory = PersistenceClientFallbackFactory.class)
public interface PersistenceClient {

    // region engine-execution

    @PatchMapping("/engine-execution/update-status/{id}")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionExtInfoById(@RequestBody EngineExecutionUpdateRequest updateRequest);

    // endregion
}
