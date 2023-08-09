package org.xi.maple.datacalc.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.datacalc.api.client.fallback.EngineExecutionClientFallback;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

@FeignClient(value = "maple-persistence-service", fallback = EngineExecutionClientFallback.class)
public interface EngineExecutionClient {
    @PostMapping("/add")
    Integer add(@Validated @RequestBody EngineExecutionAddRequest engineExecution);
}
