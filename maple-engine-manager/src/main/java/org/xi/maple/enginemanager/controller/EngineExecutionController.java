package org.xi.maple.enginemanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.enginemanager.engine.builder.EngineBuilder;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

import java.io.IOException;

@RestController
@RequestMapping("engine-execution")
public class EngineExecutionController {

    @Autowired
    EngineBuilder engineBuilder;

    @PostMapping("execute")
    public void execute(EngineExecutionAddRequest execution) throws IOException {
        engineBuilder.execute(execution);
    }
}
