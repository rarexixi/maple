package org.xi.maple.scheduler.controller;

import org.springframework.web.bind.annotation.*;
import org.xi.maple.scheduler.service.ExecutionService;

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
}
