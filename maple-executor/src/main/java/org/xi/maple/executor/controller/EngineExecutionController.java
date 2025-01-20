package org.xi.maple.executor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.executor.service.EngineExecutionService;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;

@RequestMapping(EngineExecutionController.BASE_URL)
@RestController
public class EngineExecutionController {

    public static final String BASE_URL = "/api/engine-execution";

    final EngineExecutionService engineExecutionService;

    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    @PostMapping("/execute")
    public ResponseEntity<Void> execute(@RequestBody EngineExecutionDetailResp execution) {
        engineExecutionService.execute(execution);
        return ResponseEntity.ok().build();
    }
}
