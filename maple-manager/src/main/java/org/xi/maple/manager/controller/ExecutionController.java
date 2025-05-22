package org.xi.maple.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.manager.service.ExecutionService;

import javax.validation.constraints.NotBlank;
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
        executionService.submitToCluster(execId);
    }

    @PatchMapping("/kill/{id}")
    public ResponseEntity<Object> killExecution(@PathVariable("id") Integer id,
                                                @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app) {
        return ResponseEntity.ok(executionService.kill(id, app));
    }

    @PatchMapping("/{action}/{id}")
    public ResponseEntity<Void> operateExecution(@PathVariable("action") String action,
                                                 @PathVariable("id") Integer id,
                                                 @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app,
                                                 @RequestBody Map<String, ?> params) {
        executionService.operate(id, action, app, params);
        return ResponseEntity.ok(null);
    }
}
