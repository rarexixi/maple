package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterQueryReq;
import org.xi.maple.persistence.model.request.ClusterSaveReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.ClusterItemResp;
import org.xi.maple.persistence.service.ClusterService;

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
        String detailPath = String.format("%s/%s", BASE_URL, detail.getName());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{name}")
    public ResponseEntity<Integer> deleteByName(
            @PathVariable("name") @NotBlank(message = "name(集群名称)不能为空") String name,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.deleteByName(name, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{name}")
    public ResponseEntity<Integer> disableByName(
            @PathVariable("name") @NotBlank(message = "name(集群名称)不能为空") String name,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.disableByName(name, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{name}")
    public ResponseEntity<Integer> enableByName(
            @PathVariable("name") @NotBlank(message = "name(集群名称)不能为空") String name,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterService.enableByName(name, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PutMapping("/{name}")
    public ResponseEntity<ClusterDetailResp> updateByName(
            @PathVariable("name") @Validated @NotBlank(message = "name(集群名称)不能为空") String name,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterSaveReq cluster
    ) {
        ClusterDetailResp detail = clusterService.updateByName(name, cluster);
        return ResponseEntity.ok(detail);
    }

    @PatchMapping("/{name}")
    public ResponseEntity<ClusterDetailResp> patchByName(
            @PathVariable("name") @Validated @NotBlank(message = "name(集群名称)不能为空") String name,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterSaveReq cluster
    ) {
        ClusterDetailResp detail = clusterService.patchByName(name, cluster);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{name}")
    public ResponseEntity<ClusterDetailResp> getByName(
            @PathVariable("name") @Validated @NotBlank(message = "name(集群名称)不能为空") String name
    ) {
        ClusterDetailResp detail = clusterService.getByName(name);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<ClusterItemResp>> getList(ClusterQueryReq queryReq) {
        return ResponseEntity.ok(clusterService.getList(queryReq));
    }
}
