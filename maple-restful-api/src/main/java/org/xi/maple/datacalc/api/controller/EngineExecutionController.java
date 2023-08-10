package org.xi.maple.datacalc.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.datacalc.api.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

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
    public ResponseEntity<Integer> submitJob(@RequestBody EngineExecutionAddRequest addRequest) {

        Integer id = engineExecutionService.submit(addRequest);
        return ResponseEntity.ok(id);
    }

}
