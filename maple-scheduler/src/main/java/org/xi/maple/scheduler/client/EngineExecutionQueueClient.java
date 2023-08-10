package org.xi.maple.scheduler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.scheduler.client.fallback.EngineExecutionQueueClientFallback;

import javax.validation.constraints.NotBlank;
import java.util.List;

@FeignClient(value = "maple-persistence-service", fallback = EngineExecutionQueueClientFallback.class)
public interface EngineExecutionQueueClient {

    @PostMapping("/add-or-update")
    Integer addOrUpdate(@Validated @RequestBody EngineExecutionQueueSaveRequest engineExecutionQueue);

    @DeleteMapping("/delete")
    Integer delete(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/detail")
    EngineExecutionQueue getByQueueName(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/list")
    List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest);
}
