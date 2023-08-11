package org.xi.maple.datacalc.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.datacalc.api.client.fallback.EngineExecutionClientFallbackFactory;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@FeignClient(value = "maple-persistence-service", fallbackFactory = EngineExecutionClientFallbackFactory.class)
public interface EngineExecutionClient {

    @PostMapping("/add")
    Integer add(@Validated @RequestBody EngineExecutionAddRequest engineExecution);

    @GetMapping("/detail")
    EngineExecutionDetailResponse getById(
            @RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id);

    @PatchMapping("/update-status")
    Integer updateStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateRequest);
}
