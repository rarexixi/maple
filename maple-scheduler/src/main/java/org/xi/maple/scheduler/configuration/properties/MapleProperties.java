package org.xi.maple.scheduler.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.xi.maple.common.model.MapleJsonFormatProperties;

@Component
@ConfigurationProperties(prefix = "maple")
@Data
public class MapleProperties {

    @NestedConfigurationProperty
    private MapleJsonFormatProperties jsonFormat;
}