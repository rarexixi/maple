package org.xi.maple.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.rest.model.request.ExecReq;
import org.xi.maple.rest.model.request.JobExecReq;
import org.xi.maple.rest.service.ExecutionService;
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

    /**
     * 提交执行，会先添加到队列，再等待调度器执行
     *
     * @param execReq
     * @return 执行ID
     */
    // @MapleAppAuthentication(app = "#execReq.fromApp", value = "#execReq.execUniqId + '#;' + #execReq.execName")
    @PostMapping("submit")
    public ResponseEntity<Integer> submit(@RequestBody ExecReq execReq) {
        Integer id = executionService.submit(execReq);
        return ResponseEntity.ok(id);
    }

    /**
     * 提交作业执行，会从作业解析出来执行内容，然后添加到队列，再等待调度器执行
     *
     * @param jobExecReq
     * @return 执行ID
     */
    // @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("submit-job")
    public ResponseEntity<Integer> submitJob(@RequestBody JobExecReq jobExecReq) {
        Integer id = executionService.submitJob(jobExecReq);
        return ResponseEntity.ok(id);
    }

    /**
     * 提交执行，会立即提交到集群执行
     *
     * @param execReq
     * @return 执行ID
     */
    // @MapleAppAuthentication(app = "#execReq.fromApp", value = "#execReq.execUniqId + '#;' + #execReq.execName")
    @PostMapping("exec")
    public ResponseEntity<Integer> exec(@RequestBody ExecReq execReq) {
        Integer id = executionService.exec(execReq);
        return ResponseEntity.ok(id);
    }

    /**
     * 提交作业执行，会从作业解析出来执行内容，然后会立即提交到集群执行
     *
     * @param jobExecReq
     * @return 执行ID
     */
    // @MapleAppAuthentication(app = "#addRequest.fromApp", value = "#submitReq.execUniqId + '#;' + #submitReq.execName")
    @PostMapping("exec-job")
    public ResponseEntity<Integer> execJob(@RequestBody JobExecReq jobExecReq) {
        Integer id = executionService.execJob(jobExecReq);
        return ResponseEntity.ok(id);
    }

    // @MapleAppAuthentication("#id")
    @PutMapping("{id}/kill")
    public ResponseEntity<Object> kill(@PathVariable("id") Integer id,
                                       @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app) {
        Object result = executionService.kill(id, app);
        return ResponseEntity.ok(result);
    }

    // @MapleAppAuthentication("#id")
    @PutMapping("{id}/{action}")
    public ResponseEntity<Object> operate(@PathVariable("id") Integer id,
                                          @PathVariable("action") String action,
                                          @RequestParam("app") @NotBlank(message = "app(来源应用)不能为空") String app,
                                          @RequestBody Map<String, ?> params) {
        Object result = executionService.operate(id, action, params, app);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<EngineExecutionDetailResp> detail(@PathVariable("id") Integer id) {
        EngineExecutionDetailResp detail = executionService.detail(id);
        return ResponseEntity.ok(detail);
    }
}
