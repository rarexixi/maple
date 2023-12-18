package org.xi.maple.execution.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.execution.builder.spi.EnginePluginService;

@RestController
@RequestMapping("plugin")
public class PluginController {
    final EnginePluginService enginePluginService;

    public PluginController(EnginePluginService enginePluginService) {
        this.enginePluginService = enginePluginService;
    }

    @GetMapping("refresh")
    public ResponseEntity<Void> refreshPlugins() {
        enginePluginService.refreshPluginConvertors();
        return ResponseEntity.ok().build();
    }
}
