package org.xi.maple.manager.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.manager.client.fallback.ExecutionManagerClientFallbackFactory;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

@FeignClient(value = MapleServiceName.EXECUTOR, fallbackFactory = ExecutionManagerClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.EXECUTOR, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface ExecutionManagerClient {

    // region engine-execution

    @PostMapping("/engine-execution/execute")
    void execute(EngineExecutionDetailResponse execution);

    // endregion
}
