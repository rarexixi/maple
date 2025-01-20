package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.manager.service.ExecutionService;

import java.util.Map;

@RequestMapping(ExecutionController.BASE_URL)
@RestController
public class ExecutionController {

    public static final String BASE_URL = "/api/execution";

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/exec-now")
    public void submitExecution(@RequestParam("execId") int execId) {
        executionService.submitExecution(execId);
    }

    @PatchMapping("/kill/{id}")
    public ResponseEntity<Object> killExecution(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(executionService.kill(id));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelExecution(@PathVariable("id") Integer id, @RequestBody Map<String, ?> cancelParams) {
        return ResponseEntity.ok(executionService.stop(id, cancelParams));
    }
}
