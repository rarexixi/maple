package org.xi.maple.executor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.executor.service.EngineExecutionService;
import org.xi.maple.persistence.model.response.EngineExecutionAction;

@RequestMapping(EngineExecutionController.BASE_URL)
@RestController
public class EngineExecutionController {

    public static final String BASE_URL = "/api/engine-execution";

    final EngineExecutionService engineExecutionService;

    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    @PostMapping("/execute")
    public ResponseEntity<Void> execute(@RequestBody EngineExecutionAction execution) throws Exception {
        engineExecutionService.execute(execution);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/operate")
    public ResponseEntity<Void> operate(@RequestBody EngineExecutionAction action) throws Exception {
        engineExecutionService.operate(action);
        return ResponseEntity.ok().build();
    }
}
