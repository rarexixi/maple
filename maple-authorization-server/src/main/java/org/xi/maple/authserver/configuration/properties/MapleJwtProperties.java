package org.xi.maple.authserver.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "maple.security")
@Data
public class MapleJwtProperties {
    public String privateKey;
    public String publicKey;
    public Long expiration = 3600000L;
}
