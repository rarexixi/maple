package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.request.ClusterEngineQueryReq;
import org.xi.maple.persistence.model.request.ClusterEngineSaveReq;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResp;
import org.xi.maple.persistence.model.response.ClusterEngineItemResp;
import org.xi.maple.persistence.service.ClusterEngineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(ClusterEngineController.BASE_URL)
@RestController
@Validated
public class ClusterEngineController {

    public static final String BASE_URL = "/api/cluster-engines";

    private final ClusterEngineService clusterEngineService;

    @Autowired
    public ClusterEngineController(ClusterEngineService clusterEngineService) {
        this.clusterEngineService = clusterEngineService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<ClusterEngineDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) ClusterEngineSaveReq clusterEngine) {
        ClusterEngineDetailResp detail = clusterEngineService.create(clusterEngine);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getId());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(
            @PathVariable("id") @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterEngineService.deleteById(id, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PutMapping("/{id}")
    public ResponseEntity<ClusterEngineDetailResp> updateById(
            @PathVariable("id") @Validated @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterEngineSaveReq clusterEngine
    ) {
        ClusterEngineDetailResp detail = clusterEngineService.updateById(id, clusterEngine);
        return ResponseEntity.ok(detail);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClusterEngineDetailResp> patchById(
            @PathVariable("id") @Validated @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterEngineSaveReq clusterEngine
    ) {
        ClusterEngineDetailResp detail = clusterEngineService.patchById(id, clusterEngine);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<ClusterEngineDetailResp> getById(
            @PathVariable("id") @Validated @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id
    ) {
        ClusterEngineDetailResp detail = clusterEngineService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // todo
    @GetMapping("/{id}/conf")
    public ResponseEntity<EngineConf> getEngineConf(ClusterEngineDefaultConfGetRequest request) {
        EngineConf detail = clusterEngineService.getEngineConf(request);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<ClusterEngineItemResp>> getList(ClusterEngineQueryReq queryReq) {
        return ResponseEntity.ok(clusterEngineService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<ClusterEngineItemResp>> getPageList(
            ClusterEngineQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(clusterEngineService.getPageList(queryReq, pageNum, pageSize));
    }
}
