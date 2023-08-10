package org.xi.maple.persistence.controller;

import org.xi.maple.common.model.OperateResult;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.persistence.service.EngineExecutionQueueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RequestMapping("/engine-execution-queue")
@RestController
@Validated
public class EngineExecutionQueueController {

    private final EngineExecutionQueueService engineExecutionQueueService;

    @Autowired
    public EngineExecutionQueueController(EngineExecutionQueueService engineExecutionQueueService) {
        this.engineExecutionQueueService = engineExecutionQueueService;
    }

    @PostMapping("/add-or-update")
    public ResponseEntity<OperateResult<Integer>> add(@Validated @RequestBody EngineExecutionQueueSaveRequest engineExecutionQueue) {
        OperateResult<Integer> result = engineExecutionQueueService.addOrUpdate(engineExecutionQueue);
        return ResponseEntity.created(URI.create("")).body(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName) {
        Integer count = engineExecutionQueueService.delete(queueName);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/detail")
    public ResponseEntity<EngineExecutionQueue> getByQueueName(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName) {
        EngineExecutionQueue detail = engineExecutionQueueService.getByQueueName(queueName);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<EngineExecutionQueue>> getList(EngineExecutionQueueQueryRequest queryRequest) {
        return ResponseEntity.ok(engineExecutionQueueService.getList(queryRequest));
    }
}
