package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.manager.yarn.service.YarnClusterService;

@RequestMapping(YarnController.BASE_URL)
@RestController
public class YarnController {

    public static final String BASE_URL = "/api/yarn";

    private final YarnClusterService clusterService;

    public YarnController(YarnClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PatchMapping("/{clusterId}/kill")
    public ResponseEntity<Object> delete(
            @PathVariable("clusterId") Integer clusterId,
            @RequestParam("applicationId") String applicationId) {
        Object result = clusterService.kill(clusterId, applicationId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/start-scheduler")
    public ResponseEntity<Object> startScheduler() {
        clusterService.startRefreshScheduler();
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/stop-scheduler")
    public ResponseEntity<Object> stopScheduler() {
        clusterService.stopRefreshScheduler();
        return ResponseEntity.accepted().build();
    }
}
