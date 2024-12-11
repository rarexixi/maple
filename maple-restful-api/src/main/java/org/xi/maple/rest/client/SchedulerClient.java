package org.xi.maple.rest.client;

import feign.FeignException;
import feign.Request;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.Map;

@FeignClient(value = "MAPLE-MP-SERVICE", configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = "MAPLE-MP-SERVICE", configuration = RandomRouteLoadBalancerConfiguration.class)
public interface SchedulerClient {

    @CircuitBreaker(name = "exampleService", fallbackMethod = "someApiMethodFallback")
    // region scheduler
    @GetMapping("/execution/{execId}")
    ResponseEntity<?> getExec(@PathVariable("execId") int execId);

    // region scheduler
    @PutMapping("/execution/exec-now")
    void submitExecution(@RequestParam("execId") int execId);

    @PutMapping("/execution/kill/{id}")
    Object killExecution(@PathVariable("id") Integer id);

    @PutMapping("/execution/stop/{id}")
    Object stopExecution(@PathVariable("id") Integer id, Map<String,?> cancelParams);

    // endregion

    Logger logger = LoggerFactory.getLogger(SchedulerClient.class);

    default ResponseEntity<?> someApiMethodFallback(Throwable t) throws Throwable {
        if (t instanceof FeignException.FeignClientException) {
            FeignException.FeignClientException fe = (FeignException.FeignClientException) t;
            logger.error("FeignClientException: {}", fe.getMessage());
        }
        throw t;
    }
}
