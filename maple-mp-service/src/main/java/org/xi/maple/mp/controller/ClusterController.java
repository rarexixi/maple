package org.xi.maple.mp.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.ClusterQueryReq;
import org.xi.maple.mp.model.request.ClusterSaveReq;
import org.xi.maple.mp.model.response.ClusterDetailResp;
import org.xi.maple.mp.model.response.ClusterItemResp;
import org.xi.maple.mp.service.ClusterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(ClusterController.BASE_URL)
@RestController
@Validated
public class ClusterController {

    public static final String BASE_URL = "/api/clusters";

    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<ClusterDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) ClusterSaveReq cluster) {
        ClusterDetailResp detail = clusterService.create(cluster);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getId());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{idList}")
    public ResponseEntity<Integer> deleteById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.deleteById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{idList}")
    public ResponseEntity<Integer> disableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.disableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{idList}")
    public ResponseEntity<Integer> enableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.enableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PatchMapping("/{id}")
    public ResponseEntity<ClusterDetailResp> patchById(
            @PathVariable("id") @NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterSaveReq cluster
    ) {
        ClusterDetailResp detail = clusterService.patchById(id, cluster);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClusterDetailResp> updateById(
            @PathVariable("id") @NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterSaveReq cluster
    ) {
        ClusterDetailResp detail = clusterService.updateById(id, cluster);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<ClusterDetailResp> getById(
            @PathVariable("id") @NotNull(message = "id(集群ID)不能为空") @Min(value = 1, message = "id(集群ID)必须大于0") Integer id
    ) {
        ClusterDetailResp detail = clusterService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<ClusterItemResp>> getList(ClusterQueryReq queryReq) {
        return ResponseEntity.ok(clusterService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<ClusterItemResp>> getPageList(
            ClusterQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(clusterService.getPageList(queryReq, pageNum, pageSize));
    }
}
