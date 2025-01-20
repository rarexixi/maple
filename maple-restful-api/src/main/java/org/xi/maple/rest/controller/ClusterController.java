package org.xi.maple.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.rest.client.ManagerClient;
import org.xi.maple.rest.service.ClusterService;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    final ClusterService clusterService;
    final ManagerClient managerClient;

    public ClusterController(ClusterService clusterService, ManagerClient managerClient) {
        this.clusterService = clusterService;
        this.managerClient = managerClient;
    }

    @RequestMapping
    public ResponseEntity<Integer> refresh(String clusterName) {
        int refresh = clusterService.refresh(clusterName);
        return ResponseEntity.ok(refresh);
    }
}
