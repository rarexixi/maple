package org.xi.maple.scheduler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.scheduler.client.fallback.EngineExecutionClientFallback;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@FeignClient(value = "maple-persistence-service", fallback = EngineExecutionClientFallback.class)
public interface EngineExecutionClient {

    @PostMapping("/add")
    Integer add(@Validated @RequestBody EngineExecutionAddRequest engineExecution);

    @GetMapping("/detail")
    EngineExecutionDetailResponse getById(
            @RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id);

    @PatchMapping("/update-status")
    Integer updateStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateRequest);
}
