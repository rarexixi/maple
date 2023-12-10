package org.xi.maple.execution.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.execution.builder.DefaultEngineBuilder;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

@RestController
@RequestMapping("engine-execution")
public class EngineExecutionController {

    @PostMapping("execute")
    public Object execute(EngineExecutionDetailResponse execution) {
        // return defaultEngineBuilder.execute(execution);
        return null;
    }
}
