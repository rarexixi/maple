package org.xi.maple.rest.configuration;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.bulkhead.internal.InMemoryBulkheadRegistry;
import io.github.resilience4j.bulkhead.internal.InMemoryThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadProvider;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ReactiveResilience4JCircuitBreakerConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerFactory() {
        //自定义断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
                // 滑动窗口的类型为时间窗口
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                // 时间窗口的大小为60秒
                .slidingWindowSize(60)
                // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
                .minimumNumberOfCalls(5)
                // 在单位时间窗口内调用失败率达到70%后会启动断路器
                .failureRateThreshold(70)
                // 允许断路器自动由打开状态转换为半开状态
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // 在半开状态下允许进行正常调用的次数
                .permittedNumberOfCallsInHalfOpenState(5)
                // 断路器打开状态转换为半开状态需要等待60秒
                .waitDurationInOpenState(Duration.ofSeconds(60))
                // 所有异常都当作失败来处理
                .recordExceptions(Throwable.class)
                .build();
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                //超时规则,默认1s
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(20)).build())
                //设置断路器配置
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

    @Bean
    public Resilience4jBulkheadProvider defaultBulkheadCustomizer() {
        ThreadPoolBulkheadConfig threadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
                //最大线程数
                .maxThreadPoolSize(500)
                //核心线程数
                .coreThreadPoolSize(100)
                //拒绝策略
                .rejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy())
                //线程存活时间
                .keepAliveDuration(Duration.ofMillis(20L))
                //队列大小
                .queueCapacity(Integer.MAX_VALUE)
                .build();
        ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry = new InMemoryThreadPoolBulkheadRegistry(threadPoolBulkheadConfig);
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
                //最大并发数量
                .maxConcurrentCalls(300)
                //最大等待时间
                .maxWaitDuration(Duration.ofMillis(500))
                .build();
        BulkheadRegistry bulkheadRegistry = new InMemoryBulkheadRegistry(bulkheadConfig);
        return new Resilience4jBulkheadProvider(threadPoolBulkheadRegistry, bulkheadRegistry);
    }
}