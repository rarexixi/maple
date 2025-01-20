package org.xi.maple.manager.controller;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.manager.k8s.service.K8sClusterService;

import java.util.List;

@RequestMapping(K8sController.BASE_URL)
@RestController
public class K8sController {

    public static final String BASE_URL = "/api/k8s";

    private final K8sClusterService clusterService;

    public K8sController(K8sClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PutMapping("/{clusterId}/deploy-by-file")
    public ResponseEntity<List<HasMetadata>> deployByFile(
            @PathVariable("clusterId") Integer clusterId,
            @RequestParam("yamlFile") MultipartFile yamlFile) {
        List<HasMetadata> result = clusterService.deployEngine(clusterId, yamlFile);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{clusterId}/delete-by-file")
    public ResponseEntity<List<StatusDetails>> deleteByFile(
            @PathVariable("clusterId") Integer clusterId,
            @RequestParam("yamlFile") MultipartFile yamlFile) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterId, yamlFile);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{clusterId}/delete-by-name")
    public ResponseEntity<List<StatusDetails>> deleteByName(
            @PathVariable("clusterId") Integer clusterId,
            @RequestParam("namespace") String namespace,
            @RequestParam("type") String type,
            @RequestParam("name") String name) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterId, namespace, type, name);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{clusterId}/deploy")
    public ResponseEntity<List<HasMetadata>> deploy(
            @PathVariable("clusterId") Integer clusterId,
            @RequestBody String yaml) {
        List<HasMetadata> result = clusterService.deployEngine(clusterId, yaml);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{clusterId}/delete")
    public ResponseEntity<List<StatusDetails>> delete(
            @PathVariable("clusterId") Integer clusterId,
            @RequestBody String yaml) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterId, yaml);
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
