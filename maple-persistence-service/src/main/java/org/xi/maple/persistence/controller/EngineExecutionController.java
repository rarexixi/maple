package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionExtUpdateReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.service.EngineExecutionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(EngineExecutionController.BASE_URL)
@RestController
@Validated
public class EngineExecutionController {

    public static final String BASE_URL = "/api/engine-executions";

    private final EngineExecutionService engineExecutionService;

    @Autowired
    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<Integer> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) EngineExecutionSaveReq engineExecution) {
        Integer id = engineExecutionService.create(engineExecution);
        String detailPath = String.format("%s/%s", BASE_URL, id);
        return ResponseEntity.created(URI.create(detailPath)).body(id);
    }

    // endregion 创建

    // region 更新

    @PatchMapping("/{id}/status")
    public ResponseEntity<Integer> updateStatusById(@PathVariable("id") Integer id, @Validated @RequestBody EngineExecutionStatusUpdateReq updateRequest) {
        int count = engineExecutionService.updateStatusById(id, updateRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}/ext-info")
    public ResponseEntity<Integer> patchExtInfoById(@PathVariable("id") Integer id, @Validated @RequestBody EngineExecutionExtUpdateReq updateRequest) {
        int count = engineExecutionService.patchExtInfoById(id, updateRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EngineExecutionDetailResp> patchById(
            @PathVariable("id") @Validated @NotNull(message = "id(执行ID)不能为空") @Min(value = 1, message = "id(执行ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) EngineExecutionSaveReq engineExecution
    ) {
        EngineExecutionDetailResp detail = engineExecutionService.patchById(id, engineExecution);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<EngineExecutionDetailResp> getById(
            @PathVariable("id") @Validated @NotNull(message = "id(执行ID)不能为空") @Min(value = 1, message = "id(执行ID)必须大于0") Integer id
    ) {
        EngineExecutionDetailResp detail = engineExecutionService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情
}
