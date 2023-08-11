package org.xi.maple.scheduler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.scheduler.client.fallback.EngineExecutionClientFallbackFactory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "maple-persistence-service", fallbackFactory = EngineExecutionClientFallbackFactory.class)
public interface PersistenceClient {

    // region engine-execution

    @PostMapping("/engine-execution/add")
    Integer add(@Validated @RequestBody EngineExecutionAddRequest engineExecution);

    @GetMapping("/engine-execution/detail")
    EngineExecutionDetailResponse getById(@RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id);

    @PatchMapping("/engine-execution/update-status")
    Integer updateStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateRequest);

    // endregion

    // region engine-execution-queue

    @PostMapping("/add-or-update")
    Integer addOrUpdate(@Validated @RequestBody EngineExecutionQueueSaveRequest engineExecutionQueue);

    @DeleteMapping("/delete")
    Integer delete(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/detail")
    EngineExecutionQueue getByQueueName(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/list")
    List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest);

    // endregion
}
