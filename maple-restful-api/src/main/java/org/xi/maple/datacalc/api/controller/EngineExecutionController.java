package org.xi.maple.datacalc.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.util.SecurityUtils;
import org.xi.maple.datacalc.api.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 作业提交 Controller
 *
 * @author xishihao
 */
@RestController
@RequestMapping("engine-execution")
public class EngineExecutionController {

    final EngineExecutionService engineExecutionService;

    public EngineExecutionController(EngineExecutionService engineExecutionService) {
        this.engineExecutionService = engineExecutionService;
    }

    @PostMapping("submit")
    public ResponseEntity<Integer> submitJob(
            @RequestParam("timestamp") String timestamp,
            @RequestParam("secret") String secret,
            @RequestBody EngineExecutionAddRequest addRequest) throws NoSuchAlgorithmException, InvalidKeyException {

        Integer id = engineExecutionService.submit(addRequest, timestamp, secret);
        return ResponseEntity.ok(id);
    }

    @GetMapping("detail")
    public ResponseEntity<EngineExecutionDetailResponse> detail(@RequestParam("id") Integer id) {
        EngineExecutionDetailResponse detail = engineExecutionService.detail(id);
        return ResponseEntity.ok(detail);
    }
}
