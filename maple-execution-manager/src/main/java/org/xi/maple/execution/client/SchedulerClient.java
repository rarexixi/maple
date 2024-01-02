package org.xi.maple.execution.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.execution.client.fallback.SchedulerClientFallbackFactory;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.List;
import java.util.Map;

@FeignClient(value = MapleServiceName.SCHEDULER_SERVICE, fallbackFactory = SchedulerClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.SCHEDULER_SERVICE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("/k8s/{clusterName}/deploy")
    List<Map<String, ?>> deploy(@PathVariable("clusterName") String clusterName, @RequestBody String yaml);

    // endregion
}
