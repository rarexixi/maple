package org.xi.maple.datacalc.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.datacalc.api.service.ExecutionService;
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

    @PostMapping("exec")
    public ResponseEntity<Integer> submit(
            // @RequestParam("timestamp") @NotNull(message = "时间辍不能为空") Long timestamp,
            // @RequestParam("secret") @NotBlank(message = "加密字符串不能为空") String secret,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("secret") String secret,
            @RequestBody EngineExecutionAddRequest addRequest) {
        Integer id = executionService.submit(addRequest, timestamp, secret);
        return ResponseEntity.ok(id);
    }

    @PostMapping("exec-now")
    public ResponseEntity<Integer> submitNow(
            // @RequestParam("timestamp") @NotNull(message = "时间辍不能为空") Long timestamp,
            // @RequestParam("secret") @NotBlank(message = "加密字符串不能为空") String secret,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("secret") String secret,
            @RequestBody EngineExecutionAddRequest addRequest) {
        Integer id = executionService.submitNow(addRequest, timestamp, secret);
        return ResponseEntity.ok(id);
    }

    @PutMapping("kill/{id}")
    public ResponseEntity<Object> kill(
            @RequestParam("timestamp") @NotNull(message = "时间辍不能为空") Long timestamp,
            @RequestParam("secret") @NotBlank(message = "加密字符串不能为空") String secret,
            @PathVariable("id") Integer id) {
        Object result = executionService.kill(id, timestamp, secret);
        return ResponseEntity.ok(result);
    }

    @PutMapping("stop/{id}")
    public ResponseEntity<Object> stop(
            @RequestParam("timestamp") @NotNull(message = "时间辍不能为空") Long timestamp,
            @RequestParam("secret") @NotBlank(message = "加密字符串不能为空") String secret,
            @PathVariable("id") Integer id,
            @RequestBody Map<String, ?> cancelParams) {
        Object result = executionService.stop(id, timestamp, secret, cancelParams);
        return ResponseEntity.ok(result);
    }

    @GetMapping("detail")
    public ResponseEntity<EngineExecutionDetailResponse> detail(@RequestParam("id") Integer id) {
        EngineExecutionDetailResponse detail = executionService.detail(id);
        return ResponseEntity.ok(detail);
    }
}
