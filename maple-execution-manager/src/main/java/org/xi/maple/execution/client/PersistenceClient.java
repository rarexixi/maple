package org.xi.maple.execution.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.execution.client.fallback.PersistenceClientFallbackFactory;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

@FeignClient(value = MapleServiceName.PERSISTENCE_SERVICE, fallbackFactory = PersistenceClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.PERSISTENCE_SERVICE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface PersistenceClient {

    // region engine-execution

    @PatchMapping("/engine-execution/update-status/{id}")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionExtInfoById(@RequestBody EngineExecutionUpdateRequest updateRequest);

    // endregion

    // region engine

    @GetMapping("/engine/conf")
    EngineConf getEngineConf(@SpringQueryMap ClusterEngineDefaultConfGetRequest request);

    // endregion
}
