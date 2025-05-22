package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveReq;
import org.xi.maple.persistence.model.response.EngineExecutionQueueResp;
import org.xi.maple.persistence.service.EngineExecutionQueueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(EngineExecutionQueueController.BASE_URL)
@RestController
@Validated
public class EngineExecutionQueueController {

    public static final String BASE_URL = "/api/engine-execution-queues";

    private final EngineExecutionQueueService engineExecutionQueueService;

    @Autowired
    public EngineExecutionQueueController(EngineExecutionQueueService engineExecutionQueueService) {
        this.engineExecutionQueueService = engineExecutionQueueService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<OperateResult<Integer>> upsert(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) EngineExecutionQueueSaveReq engineExecutionQueue) {
        OperateResult<Integer> result = engineExecutionQueueService.upsert(engineExecutionQueue);
        String detailPath = String.format("%s/%s", BASE_URL, engineExecutionQueue.getQueueName());
        return ResponseEntity.created(URI.create(detailPath)).body(result);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{queueName}")
    public ResponseEntity<Integer> deleteByQueueName(
            @PathVariable("queueName") @NotBlank(message = "queueName(执行队列名)不能为空") String queueName,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = engineExecutionQueueService.deleteByQueueName(queueName, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<EngineExecutionQueueResp>> getList() {
        return ResponseEntity.ok(engineExecutionQueueService.getList());
    }
}
