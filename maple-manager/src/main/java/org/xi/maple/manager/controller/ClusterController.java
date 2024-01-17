package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.manager.service.ExecutionService;

@RequestMapping("cluster")
@RestController
public class ClusterController {

    private final ExecutionService executionService;

    public ClusterController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PutMapping("refresh")
    public ResponseEntity<Void> refresh(@RequestBody ClusterMessage clusterMessage) {
        executionService.refreshCluster(clusterMessage);
        return ResponseEntity.accepted().build();
    }
}
