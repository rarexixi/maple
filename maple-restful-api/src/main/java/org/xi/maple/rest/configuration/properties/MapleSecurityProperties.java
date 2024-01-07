package org.xi.maple.rest.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "maple.security")
@Data
public class MapleSecurityProperties {

    private Boolean appCheck = false;
}
