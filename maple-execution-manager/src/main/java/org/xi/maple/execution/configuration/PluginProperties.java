package org.xi.maple.execution.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "plugin")
@Data
@Validated
public class PluginProperties {

    @NotBlank
    private String home;

    @NotBlank
    private String ftlPath;
}
