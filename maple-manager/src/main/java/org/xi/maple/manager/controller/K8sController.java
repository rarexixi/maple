package org.xi.maple.manager.controller;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.manager.k8s.service.K8sClusterService;

import java.util.List;

@RequestMapping("k8s")
@RestController
public class K8sController {

    private final K8sClusterService clusterService;

    public K8sController(K8sClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PutMapping("{clusterName}/deploy-by-file")
    public ResponseEntity<List<HasMetadata>> deployByFile(
            @PathVariable("clusterName") String clusterName,
            @RequestParam("yamlFile") MultipartFile yamlFile) {
        List<HasMetadata> result = clusterService.deployEngine(clusterName, yamlFile);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{clusterName}/delete-by-file")
    public ResponseEntity<List<StatusDetails>> deleteByFile(
            @PathVariable("clusterName") String clusterName,
            @RequestParam("yamlFile") MultipartFile yamlFile) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterName, yamlFile);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{clusterName}/delete-by-name")
    public ResponseEntity<List<StatusDetails>> deleteByName(
            @PathVariable("clusterName") String clusterName,
            @RequestParam("namespace") String namespace,
            @RequestParam("type") String type,
            @RequestParam("name") String name) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterName, namespace, type, name);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{clusterName}/deploy")
    public ResponseEntity<List<HasMetadata>> deploy(
            @PathVariable("clusterName") String clusterName,
            @RequestBody String yaml) {
        List<HasMetadata> result = clusterService.deployEngine(clusterName, yaml);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{clusterName}/delete")
    public ResponseEntity<List<StatusDetails>> delete(
            @PathVariable("clusterName") String clusterName,
            @RequestBody String yaml) {
        List<StatusDetails> result = clusterService.deleteEngine(clusterName, yaml);
        return ResponseEntity.ok(result);
    }
}
