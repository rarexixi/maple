package org.xi.maple.executor.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

@FeignClient(value = MapleServiceName.PERSISTENCE, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.PERSISTENCE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface PersistenceClient {

    // region engine-execution

    @PatchMapping("/engine-executions/{id}/status")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @RequestBody EngineExecutionStatusUpdateReq req);

    @PatchMapping("/engine-executions/{id}/ext-info")
    Integer updateExecutionExtInfoById(@RequestBody EngineExecutionExtUpdateReq req);

    // endregion

    // region cluster-engine

    @GetMapping("/cluster-engines/conf")
    EngineConf getEngineConf(@SpringQueryMap ClusterEngineDefaultConfGetRequest req);

    // endregion
}
