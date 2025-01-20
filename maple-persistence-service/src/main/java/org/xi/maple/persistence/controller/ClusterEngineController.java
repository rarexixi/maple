package org.xi.maple.persistence.controller;

import org.xi.maple.common.model.EngineConf;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.service.ClusterEngineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    // region 详情

    // todo
    @GetMapping("/{id}/conf")
    public ResponseEntity<EngineConf> getEngineConf(@PathVariable("id") Integer id, ClusterEngineDefaultConfGetRequest request) {
        EngineConf detail = clusterEngineService.getEngineConf(id, request);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情
}
