package org.xi.maple.datacalc.api.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.datacalc.api.client.fallback.SchedulerClientFallbackFactory;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.Map;

@FeignClient(value = MapleServiceName.SCHEDULER_SERVICE, fallbackFactory = SchedulerClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.SCHEDULER_SERVICE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface SchedulerClient {

    // region scheduler

    @PutMapping("exec-now")
    void submitExecution(@RequestParam("execId") int execId);

    @PutMapping("kill/{id}")
    Object killExecution(@PathVariable("id") Integer id);

    @PutMapping("stop/{id}")
    Object stopExecution(@PathVariable("id") Integer id, Map<String,?> cancelParams);

    // endregion
}
