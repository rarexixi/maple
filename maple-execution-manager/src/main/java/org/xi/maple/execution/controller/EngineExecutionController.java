package org.xi.maple.execution.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.execution.builder.EngineBuilder;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

@RestController
@RequestMapping("engine-execution")
public class EngineExecutionController {

    @Autowired
    EngineBuilder engineBuilder;

    @PostMapping("execute")
    public void execute(EngineExecutionDetailResponse execution) {
        engineBuilder.execute(execution);
    }
}
