package org.xi.maple.mp.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.DatasourceTypeQueryReq;
import org.xi.maple.mp.model.request.DatasourceTypeSaveReq;
import org.xi.maple.mp.model.response.DatasourceTypeDetailResp;
import org.xi.maple.mp.model.response.DatasourceTypeItemResp;
import org.xi.maple.mp.service.DatasourceTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(DatasourceTypeController.BASE_URL)
@RestController
@Validated
public class DatasourceTypeController {

    public static final String BASE_URL = "/api/datasource-types";

    private final DatasourceTypeService datasourceTypeService;

    @Autowired
    public DatasourceTypeController(DatasourceTypeService datasourceTypeService) {
        this.datasourceTypeService = datasourceTypeService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<DatasourceTypeDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) DatasourceTypeSaveReq datasourceType) {
        DatasourceTypeDetailResp detail = datasourceTypeService.create(datasourceType);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getTypeCode());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{typeCodeList}")
    public ResponseEntity<Integer> deleteByTypeCode(
            @PathVariable("typeCodeList") @Validated List<@NotBlank(message = "typeCode(类型编码)不能为空") String> typeCodeList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceTypeService.deleteByTypeCode(typeCodeList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{typeCodeList}")
    public ResponseEntity<Integer> disableByTypeCode(
            @PathVariable("typeCodeList") @Validated List<@NotBlank(message = "typeCode(类型编码)不能为空") String> typeCodeList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceTypeService.disableByTypeCode(typeCodeList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{typeCodeList}")
    public ResponseEntity<Integer> enableByTypeCode(
            @PathVariable("typeCodeList") @Validated List<@NotBlank(message = "typeCode(类型编码)不能为空") String> typeCodeList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceTypeService.enableByTypeCode(typeCodeList, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PatchMapping("/{typeCode}")
    public ResponseEntity<DatasourceTypeDetailResp> patchByTypeCode(
            @PathVariable("typeCode") @NotBlank(message = "typeCode(类型编码)不能为空") String typeCode,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) DatasourceTypeSaveReq datasourceType
    ) {
        DatasourceTypeDetailResp detail = datasourceTypeService.patchByTypeCode(typeCode, datasourceType);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{typeCode}")
    public ResponseEntity<DatasourceTypeDetailResp> updateByTypeCode(
            @PathVariable("typeCode") @NotBlank(message = "typeCode(类型编码)不能为空") String typeCode,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) DatasourceTypeSaveReq datasourceType
    ) {
        DatasourceTypeDetailResp detail = datasourceTypeService.updateByTypeCode(typeCode, datasourceType);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{typeCode}")
    public ResponseEntity<DatasourceTypeDetailResp> getByTypeCode(
            @PathVariable("typeCode") @NotBlank(message = "typeCode(类型编码)不能为空") String typeCode
    ) {
        DatasourceTypeDetailResp detail = datasourceTypeService.getByTypeCode(typeCode);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<DatasourceTypeItemResp>> getList(DatasourceTypeQueryReq queryReq) {
        return ResponseEntity.ok(datasourceTypeService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<DatasourceTypeItemResp>> getPageList(
            DatasourceTypeQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(datasourceTypeService.getPageList(queryReq, pageNum, pageSize));
    }
}
