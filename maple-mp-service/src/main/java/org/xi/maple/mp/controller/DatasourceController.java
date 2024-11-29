package org.xi.maple.mp.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.DatasourceQueryReq;
import org.xi.maple.mp.model.request.DatasourceSaveReq;
import org.xi.maple.mp.model.response.DatasourceDetailResp;
import org.xi.maple.mp.model.response.DatasourceItemResp;
import org.xi.maple.mp.service.DatasourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(DatasourceController.BASE_URL)
@RestController
@Validated
public class DatasourceController {

    public static final String BASE_URL = "/api/datasources";

    private final DatasourceService datasourceService;

    @Autowired
    public DatasourceController(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<DatasourceDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) DatasourceSaveReq datasource) {
        DatasourceDetailResp detail = datasourceService.create(datasource);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getId());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{idList}")
    public ResponseEntity<Integer> deleteById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceService.deleteById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{idList}")
    public ResponseEntity<Integer> disableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceService.disableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{idList}")
    public ResponseEntity<Integer> enableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = datasourceService.enableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PatchMapping("/{id}")
    public ResponseEntity<DatasourceDetailResp> patchById(
            @PathVariable("id") @NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) DatasourceSaveReq datasource
    ) {
        DatasourceDetailResp detail = datasourceService.patchById(id, datasource);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatasourceDetailResp> updateById(
            @PathVariable("id") @NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) DatasourceSaveReq datasource
    ) {
        DatasourceDetailResp detail = datasourceService.updateById(id, datasource);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<DatasourceDetailResp> getById(
            @PathVariable("id") @NotNull(message = "id(Id)不能为空") @Min(value = 1, message = "id(Id)必须大于0") Integer id
    ) {
        DatasourceDetailResp detail = datasourceService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<DatasourceItemResp>> getList(DatasourceQueryReq queryReq) {
        return ResponseEntity.ok(datasourceService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<DatasourceItemResp>> getPageList(
            DatasourceQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(datasourceService.getPageList(queryReq, pageNum, pageSize));
    }
}
