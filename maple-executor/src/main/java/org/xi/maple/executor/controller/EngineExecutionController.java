package org.xi.maple.executor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.executor.service.EngineExecutionService;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

@RestController
@RequestMapping("engine-execution")
public class EngineExecutionController {

    final EngineExecutionService engineExecutionService;

    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    @PostMapping("execute")
    public ResponseEntity<Void> execute(@RequestBody EngineExecutionDetailResponse execution) {
        engineExecutionService.execute(execution);
        return ResponseEntity.ok().build();
    }
}