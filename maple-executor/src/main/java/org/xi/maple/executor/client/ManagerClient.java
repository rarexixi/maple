package org.xi.maple.executor.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.List;
import java.util.Map;

@FeignClient(value = MapleServiceName.MANAGER, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.MANAGER, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface ManagerClient {

    // region scheduler

    @PutMapping("/k8s/{clusterId}/deploy")
    List<Map<String, ?>> deploy(@PathVariable("clusterId") Integer clusterId, @RequestBody String yaml);

    // endregion
}
