package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.manager.service.ExecutionService;

import java.util.Map;

@RequestMapping("execution")
@RestController
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PutMapping("exec-now")
    public void submitExecution(@RequestParam("execId") int execId) {
        executionService.submitExecution(execId);
    }

    @PutMapping("kill/{id}")
    public ResponseEntity<Object> killExecution(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(executionService.kill(id));
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<Object> cancelExecution(@PathVariable("id") Integer id, @RequestBody Map<String, ?> cancelParams) {
        return ResponseEntity.ok(executionService.stop(id, cancelParams));
    }


}
