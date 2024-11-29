package org.xi.maple.service.configuration;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.xi.maple.service.configuration.properties.MapleProperties;
import org.xi.maple.service.interceptor.ControllerMethodInterceptor;

@ComponentScan("org.xi.maple.service")
@Configuration
@EnableConfigurationProperties(MapleProperties.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE) // 优先加载当前 springboot-starter 中的配置，防止多个 starter 中的 @Primary 冲突，实测设置为 -1 即可
public class MapleServiceConfiguration {

    private final MapleProperties mapleProperties;

    public MapleServiceConfiguration(MapleProperties mapleProperties) {
        this.mapleProperties = mapleProperties;
    }

    @Bean
    public DefaultPointcutAdvisor controllerAdvisor(ControllerMethodInterceptor interceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(mapleProperties.getAspect().getControllerMethodPattern());
        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }
}
