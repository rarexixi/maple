package org.xi.maple.rest.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.*;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;


@FeignClient(value = MapleServiceName.PERSISTENCE, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.PERSISTENCE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface PersistenceClient {

    // region engine-execution

    @PostMapping("/api/engine-executions")
    Integer addExecution(@RequestBody EngineExecutionCreateReq req);

    @GetMapping("/api/engine-executions/{id}")
    EngineExecutionDetailResp getExecutionById(@PathVariable("id") Integer id);

    @PatchMapping("/api/engine-executions/{id}/status")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @RequestBody EngineExecutionStatusUpdateReq req);

    // endregion

    // region engine-execution-queue

    @PostMapping("/api/engine-execution-queues")
    OperateResult<Integer> upsertExecQueue(@RequestBody EngineExecutionQueueSaveReq req);

    // endregion

    // region application

    @GetMapping("/api/applications/{appName}")
    ApplicationDetailResp getApplicationByAppName(@PathVariable("appName") String appName);

    // endregion

    // region job

    @GetMapping("/api/jobs/{id}")
    JobDetailResp getJobById(@PathVariable("id") Integer id);

    // endregion
}
