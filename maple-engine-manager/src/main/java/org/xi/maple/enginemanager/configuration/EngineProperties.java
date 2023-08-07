package org.xi.maple.enginemanager.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "engine")
@Data
public class EngineProperties {

    private String execHome;
}
