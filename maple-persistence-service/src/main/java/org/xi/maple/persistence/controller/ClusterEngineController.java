package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.ClusterEngineAddRequest;
import org.xi.maple.persistence.model.request.ClusterEnginePatchRequest;
import org.xi.maple.persistence.model.request.ClusterEngineQueryRequest;
import org.xi.maple.persistence.model.request.ClusterEngineSaveRequest;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResponse;
import org.xi.maple.persistence.model.response.ClusterEngineListItemResponse;
import org.xi.maple.persistence.service.ClusterEngineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@CrossOrigin
@RequestMapping("cluster-engine")
@RestController
@Validated
public class ClusterEngineController {

    private final ClusterEngineService clusterEngineService;

    @Autowired
    public ClusterEngineController(ClusterEngineService clusterEngineService) {
        this.clusterEngineService = clusterEngineService;
    }

    @PostMapping("add")
    public ResponseEntity<ClusterEngineDetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) ClusterEngineAddRequest clusterEngine) {
        ClusterEngineDetailResponse detail = clusterEngineService.add(clusterEngine);
        return ResponseEntity.created(URI.create("")).body(detail);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) ClusterEnginePatchRequest patchRequest) {
        Integer count = clusterEngineService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }


    @PatchMapping("update")
    public ResponseEntity<ClusterEngineDetailResponse> updateById(@Validated @RequestBody @SetFieldTypes(types = {"update"}) ClusterEngineSaveRequest clusterEngine) {
        ClusterEngineDetailResponse detail = clusterEngineService.updateById(clusterEngine);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("detail")
    public ResponseEntity<ClusterEngineDetailResponse> getById(@RequestParam("id") @NotNull(message = "引擎ID不能为空") @Min(value = 1, message = "引擎ID必须大于0") Integer id) {
        ClusterEngineDetailResponse detail = clusterEngineService.getById(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("list")
    public ResponseEntity<List<ClusterEngineListItemResponse>> getList(ClusterEngineQueryRequest queryRequest) {
        return ResponseEntity.ok(clusterEngineService.getList(queryRequest));
    }

    @GetMapping("page-list")
    public ResponseEntity<PageList<ClusterEngineListItemResponse>> getPageList(
            ClusterEngineQueryRequest queryRequest,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(clusterEngineService.getPageList(queryRequest, pageNum, pageSize));
    }
}
