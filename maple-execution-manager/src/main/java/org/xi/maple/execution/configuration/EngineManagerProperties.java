package org.xi.maple.execution.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plugin")
@Data
public class EngineManagerProperties {

    private String execHome;
    private String pluginHome;
}
