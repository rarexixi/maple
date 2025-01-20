package org.xi.maple.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.annotation.MapleAppAuthentication;
import org.xi.maple.rest.model.request.ExecReq;
import org.xi.maple.rest.model.request.JobExecReq;
import org.xi.maple.rest.service.ExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;

import javax.validation.constraints.NotBlank;
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
    @PostMapping("submit")
    public ResponseEntity<Integer> submit(@RequestBody ExecReq execReq) {
        Integer id = executionService.submit(execReq);
        return ResponseEntity.ok(id);
    }

    @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("submit-job")
    public ResponseEntity<Integer> submitJob(@RequestBody JobExecReq jobExecReq) {
        Integer id = executionService.submitJob(jobExecReq);
        return ResponseEntity.ok(id);
    }

    @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("exec")
    public ResponseEntity<Integer> exec(@RequestBody ExecReq execReq) {
        Integer id = executionService.exec(execReq);
        return ResponseEntity.ok(id);
    }

    @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("exec-job")
    public ResponseEntity<Integer> execJob(@RequestBody JobExecReq jobExecReq) {
        Integer id = executionService.execJob(jobExecReq);
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
    public ResponseEntity<EngineExecutionDetailResp> detail(@RequestParam("id") Integer id) {
        EngineExecutionDetailResp detail = executionService.detail(id);
        return ResponseEntity.ok(detail);
    }
}
