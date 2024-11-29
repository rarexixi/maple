package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveReq;
import org.xi.maple.persistence.service.ClusterEngineDefaultConfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(ClusterEngineDefaultConfController.BASE_URL)
@RestController
@Validated
public class ClusterEngineDefaultConfController {

    public static final String BASE_URL = "/api/cluster-engine-default-conves";

    private final ClusterEngineDefaultConfService clusterEngineDefaultConfService;

    @Autowired
    public ClusterEngineDefaultConfController(ClusterEngineDefaultConfService clusterEngineDefaultConfService) {
        this.clusterEngineDefaultConfService = clusterEngineDefaultConfService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<Integer> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) ClusterEngineDefaultConfSaveReq clusterEngineDefaultConf) {
        Integer id = clusterEngineDefaultConfService.create(clusterEngineDefaultConf);
        String detailPath = String.format("%s/%s", BASE_URL, id);
        return ResponseEntity.created(URI.create(detailPath)).body(id);
    }

    // endregion 创建

    // region 删除

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(
            @PathVariable("id") @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = clusterEngineDefaultConfService.deleteById(id, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除

    // region 更新

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateById(
            @PathVariable("id") @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterEngineDefaultConfSaveReq clusterEngineDefaultConf
    ) {
        Integer result = clusterEngineDefaultConfService.updateById(id, clusterEngineDefaultConf);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Integer> patchById(
            @PathVariable("id") @NotNull(message = "id(引擎ID)不能为空") @Min(value = 1, message = "id(引擎ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ClusterEngineDefaultConfSaveReq clusterEngineDefaultConf
    ) {
        Integer result = clusterEngineDefaultConfService.patchById(id, clusterEngineDefaultConf);
        return ResponseEntity.ok(result);
    }

    // endregion 更新
}
