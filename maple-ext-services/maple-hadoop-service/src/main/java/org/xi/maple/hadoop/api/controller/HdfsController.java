package org.xi.maple.hadoop.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.xi.maple.hadoop.api.model.HdfsPathModel;
import org.xi.maple.hadoop.api.service.HdfsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    final HdfsService hdfsService;

    public HdfsController(HdfsService hdfsService) {
        this.hdfsService = hdfsService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<HdfsPathModel>> list(@RequestParam(value = "path", defaultValue = "/") String path) throws IOException, InterruptedException {
        return ResponseEntity.ok(hdfsService.list(path));
    }
}
