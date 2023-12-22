package org.xi.maple.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.persistence.model.request.ClusterAddRequest;
import org.xi.maple.persistence.model.request.ClusterPatchRequest;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.request.ClusterSaveRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.persistence.service.ClusterService;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RequestMapping("/cluster")
@RestController
@Validated
public class ClusterController {

    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PostMapping("/add")
    public ResponseEntity<ClusterDetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) ClusterAddRequest cluster) {
        ClusterDetailResponse detail = clusterService.add(cluster);
        return ResponseEntity.created(URI.create("")).body(detail);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) ClusterPatchRequest patchRequest) {
        Integer count = clusterService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable")
    public ResponseEntity<Integer> disable(@Validated @SetFieldTypes(types = {"update"}) ClusterPatchRequest patchRequest) {
        Integer count = clusterService.disable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Integer> enable(@Validated @SetFieldTypes(types = {"update"}) ClusterPatchRequest patchRequest) {
        Integer count = clusterService.enable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update")
    public ResponseEntity<ClusterDetailResponse> updateByName(
            @Validated @RequestBody @SetFieldTypes(types = {"update"}) ClusterSaveRequest cluster
    ) {
        ClusterDetailResponse detail = clusterService.updateByName(cluster);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/detail")
    public ResponseEntity<ClusterDetailResponse> getByName(@RequestParam("name") @NotBlank(message = "集群名称不能为空") String name) {
        ClusterDetailResponse detail = clusterService.getByName(name);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClusterListItemResponse>> getList(ClusterQueryRequest queryRequest) {
        return ResponseEntity.ok(clusterService.getList(queryRequest));
    }
}
