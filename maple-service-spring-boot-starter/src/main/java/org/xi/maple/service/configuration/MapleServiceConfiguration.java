package org.xi.maple.service.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.xi.maple.service.configuration.properties.MapleProperties;

// @EnableFeignClients("org.xi.maple.service.client")
@ComponentScan("org.xi.maple.service")
@Configuration
@EnableConfigurationProperties(MapleProperties.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE) // 优先加载当前 springboot-starter 中的配置，防止多个 starter 中的 @Primary 冲突，实测设置为 -1 即可
public class MapleServiceConfiguration {
}
