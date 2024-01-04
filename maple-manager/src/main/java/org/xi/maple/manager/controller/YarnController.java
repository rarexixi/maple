package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.manager.yarn.service.YarnClusterService;

@RequestMapping("yarn")
@RestController
public class YarnController {

    private final YarnClusterService clusterService;

    public YarnController(YarnClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PutMapping("{clusterName}/kill")
    public ResponseEntity<Object> delete(
            @PathVariable("clusterName") String clusterName,
            @RequestParam("applicationId") String applicationId) {
        Object result = clusterService.kill(clusterName, applicationId);
        return ResponseEntity.ok(result);
    }
}
