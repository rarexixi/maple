package org.xi.maple.rest.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.maple.rest.client.fallback.SchedulerClientFallbackFactory;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.Map;

@FeignClient(value = MapleServiceName.MANAGER, fallbackFactory = SchedulerClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.MANAGER, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("/execution/exec-now")
    void submitExecution(@RequestParam("execId") int execId);

    @PutMapping("/execution/kill/{id}")
    Object killExecution(@PathVariable("id") Integer id);

    @PutMapping("/execution/stop/{id}")
    Object stopExecution(@PathVariable("id") Integer id, Map<String,?> cancelParams);

    // endregion
}