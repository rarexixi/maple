package org.xi.maple.datacalc.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.util.SecurityUtils;
import org.xi.maple.datacalc.api.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

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
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > 1000 * 60 * 5) {
            throw new RuntimeException("请求已过期");
        }

        String fromApp = addRequest.getFromApp();
        String secretStr = String.join("#;", new String[]{addRequest.getUniqueId(), addRequest.getExecName(), timestamp});
        String key = getAppKey(fromApp);
        if (SecurityUtils.valid(key, secretStr, secret)) {
            throw new RuntimeException("参数验证失败");
        }

        Integer id = engineExecutionService.submit(addRequest);
        return ResponseEntity.ok(id);
    }

    private String getAppKey(String fromApp) {
        return "";
    }
}
