package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfAddRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfPatchRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfQueryRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveRequest;
import org.xi.maple.persistence.model.response.ClusterEngineDefaultConfDetailResponse;
import org.xi.maple.persistence.model.response.ClusterEngineDefaultConfListItemResponse;
import org.xi.maple.persistence.service.ClusterEngineDefaultConfService;

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
@RequestMapping("cluster-engine-default-conf")
@RestController
@Validated
public class ClusterEngineDefaultConfController {

    private final ClusterEngineDefaultConfService clusterEngineDefaultConfService;

    @Autowired
    public ClusterEngineDefaultConfController(ClusterEngineDefaultConfService clusterEngineDefaultConfService) {
        this.clusterEngineDefaultConfService = clusterEngineDefaultConfService;
    }

    @PostMapping("add")
    public ResponseEntity<Integer> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) ClusterEngineDefaultConfAddRequest clusterEngineDefaultConf) {
        int result = clusterEngineDefaultConfService.add(clusterEngineDefaultConf);
        return ResponseEntity.created(URI.create("")).body(result);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) ClusterEngineDefaultConfPatchRequest patchRequest) {
        Integer count = clusterEngineDefaultConfService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("update")
    public ResponseEntity<Integer> updateById(@Validated @RequestBody @SetFieldTypes(types = {"update"}) ClusterEngineDefaultConfSaveRequest clusterEngineDefaultConf) {
        int result = clusterEngineDefaultConfService.updateById(clusterEngineDefaultConf);
        return ResponseEntity.ok(result);
    }
}
