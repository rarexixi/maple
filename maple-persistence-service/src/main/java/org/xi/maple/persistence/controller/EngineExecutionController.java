package org.xi.maple.persistence.controller;

import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionListItemResponse;
import org.xi.maple.persistence.service.EngineExecutionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;

@CrossOrigin
@RequestMapping("/engine-execution")
@RestController
@Validated
public class EngineExecutionController {

    private final EngineExecutionService engineExecutionService;

    @Autowired
    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    @PostMapping("/add")
    public ResponseEntity<Integer> add(@Validated @RequestBody EngineExecutionAddRequest engineExecution) {
        Integer id = engineExecutionService.add(engineExecution);
        return ResponseEntity.created(URI.create("")).body(id);
    }

    @GetMapping("/heartbeat")
    public ResponseEntity<Integer> heartbeatById(@RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id) {
        int count = engineExecutionService.heartbeatById(id);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update-status")
    public ResponseEntity<Integer> updateStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateRequest) {
        int count = engineExecutionService.updateStatusById(updateRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update-ext-info")
    public ResponseEntity<Integer> updateExtInfoById(@Validated @RequestBody EngineExecutionUpdateRequest updateRequest) {
        int count = engineExecutionService.updateExtInfoById(updateRequest);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/detail")
    public ResponseEntity<EngineExecutionDetailResponse> getById(@RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id) {
        throw new RuntimeException("detail error");
        // EngineExecutionDetailResponse detail = engineExecutionService.getById(id);
        // return ResponseEntity.ok(detail);
    }

    @GetMapping("/page-list")
    public ResponseEntity<PageList<EngineExecutionListItemResponse>> getPageList(
            EngineExecutionQueryRequest queryRequest,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(engineExecutionService.getPageList(queryRequest, pageNum, pageSize));
    }
}
