package org.xi.maple.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.rest.client.SchedulerClient;
import org.xi.maple.rest.service.ClusterService;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    final ClusterService clusterService;
    final SchedulerClient schedulerClient;

    public ClusterController(ClusterService clusterService, SchedulerClient schedulerClient) {
        this.clusterService = clusterService;
        this.schedulerClient = schedulerClient;
    }

    @RequestMapping
    public ResponseEntity<Integer> refresh(String clusterName) {
        int refresh = clusterService.refresh(clusterName);
        return ResponseEntity.ok(refresh);
    }

    @RequestMapping("/exec/{id}")
    public ResponseEntity<Object> getExec(@PathVariable("id") Integer id) {
        String result = String.valueOf(schedulerClient.getExec(id));
        return ResponseEntity.ok(result);
    }
}
