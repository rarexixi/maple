package org.xi.maple.enginemanager.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plugin")
@Data
public class PluginProperties {

    private String home;
    private String ftlPath;
}
