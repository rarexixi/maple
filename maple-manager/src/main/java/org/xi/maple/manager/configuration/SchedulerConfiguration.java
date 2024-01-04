package org.xi.maple.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 调度器相关配置
 *
 * @author xishihao
 */
@Configuration
public class SchedulerConfiguration {

    @Bean
    RedisMessageListenerContainer redisContainer(final RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
