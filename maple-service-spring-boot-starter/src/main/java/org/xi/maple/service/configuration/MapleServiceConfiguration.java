package org.xi.maple.service.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.xi.maple.service.configuration.properties.MapleProperties;

// @EnableFeignClients("org.xi.maple.service.client")
@ComponentScan("org.xi.maple.service")
@Configuration
@EnableConfigurationProperties(MapleProperties.class)
public class MapleServiceConfiguration {
}
