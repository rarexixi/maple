package org.xi.maple.rest.client;

import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = MapleServiceName.MANAGER, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.MANAGER, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface ManagerClient {

    // region execution

    @PostMapping("/api/execution/exec-now")
    void submitExecution(@RequestParam("execId") int execId);

    @PatchMapping("/api/execution/kill/{id}")
    Object killExecution(@PathVariable("id") Integer id, @RequestParam("app") String app);

    @PatchMapping("/api/execution/{action}/{id}")
    Object operateExecution(@PathVariable("id") Integer id, @PathVariable("action") String action, @RequestParam("app") String app, Map<String, ?> params);

    // endregion

    // @CircuitBreaker(name = "exampleService", fallbackMethod = "someApiMethodFallback")
    // @GetMapping("/api/execution/{execId}")
    // ResponseEntity<?> getExec(@PathVariable("execId") int execId);
    //
    // Logger logger = LoggerFactory.getLogger(ManagerClient.class);
    //
    // default ResponseEntity<?> someApiMethodFallback(Throwable t) throws Throwable {
    //     if (t instanceof FeignException.FeignClientException) {
    //         FeignException.FeignClientException fe = (FeignException.FeignClientException) t;
    //         logger.error("FeignClientException: {}", fe.getMessage());
    //     }
    //     throw t;
    // }
}
