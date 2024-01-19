package org.xi.maple.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.annotation.MapleAppAuthentication;
import org.xi.maple.rest.service.ExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 作业提交 Controller
 *
 * @author xishihao
 */
@RestController
@RequestMapping("execution")
public class ExecutionController {

    final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("exec")
    public ResponseEntity<Integer> submit(@RequestBody EngineExecutionAddRequest addRequest) {
        Integer id = executionService.submit(addRequest);
        return ResponseEntity.ok(id);
    }

    @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("exec-now")
    public ResponseEntity<Integer> submitNow(@RequestBody EngineExecutionAddRequest addRequest) {
        Integer id = executionService.submitNow(addRequest);
        return ResponseEntity.ok(id);
    }

    @MapleAppAuthentication("#id")
    @PutMapping("kill/{id}")
    public ResponseEntity<Object> kill(@PathVariable("id") Integer id,
                                       @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app) {
        Object result = executionService.kill(id, app);
        return ResponseEntity.ok(result);
    }

    @MapleAppAuthentication("#id")
    @PutMapping("stop/{id}")
    public ResponseEntity<Object> stop(@PathVariable("id") Integer id, @RequestBody Map<String, ?> cancelParams,
                                       @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app) {
        Object result = executionService.stop(id, cancelParams, app);
        return ResponseEntity.ok(result);
    }

    @GetMapping("detail")
    public ResponseEntity<EngineExecutionDetailResponse> detail(@RequestParam("id") Integer id) {
        EngineExecutionDetailResponse detail = executionService.detail(id);
        return ResponseEntity.ok(detail);
    }
}
