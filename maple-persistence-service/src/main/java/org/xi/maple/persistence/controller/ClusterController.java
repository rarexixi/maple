package org.xi.maple.persistence.controller;

import org.xi.maple.persistence.model.request.ClusterQueryReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.ClusterItemResp;
import org.xi.maple.persistence.service.ClusterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ClusterDetailResp> getById(
            @PathVariable("id") @Validated @NotNull(message = "id(集群ID)不能为空") Integer id
    ) {
        ClusterDetailResp detail = clusterService.getById(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/{id}/category")
    public ResponseEntity<String> getCategoryById(
            @PathVariable("id") @Validated @NotNull(message = "id(集群ID)不能为空") Integer id
    ) {
        String category = clusterService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClusterItemResp>> getList(ClusterQueryReq queryReq) {
        return ResponseEntity.ok(clusterService.getList(queryReq));
    }
}
