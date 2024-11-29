package org.xi.maple.rest.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.*;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.List;

@FeignClient(value = MapleServiceName.PERSISTENCE, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.PERSISTENCE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface PersistenceClient {

    // region engine-execution

    @PostMapping("/api/engine-executions")
    Integer addExecution(@RequestBody EngineExecutionSaveReq req);

    @GetMapping("/api/engine-executions/{id}")
    EngineExecutionDetailResp getExecutionById(@PathVariable("id") Integer id);

    @PatchMapping("/api/engine-executions/{id}/status")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @RequestBody EngineExecutionStatusUpdateReq req);

    @PatchMapping("/api/engine-executions/{id}/ext-info")
    Integer updateExecutionInfoById(@PathVariable("id") Integer id, @RequestBody EngineExecutionExtUpdateReq req);

    // endregion

    // region engine-execution-queue

    @PostMapping("/api/engine-execution-queues")
    OperateResult<Integer> upsertExecQueue(@RequestBody EngineExecutionQueueSaveReq req);

    @DeleteMapping("/api/engine-execution-queues/{queueName}")
    Integer deleteExecQueue(@PathVariable("queueName") String queueName);

    @GetMapping("/api/engine-execution-queues/{queueName}")
    EngineExecutionQueue getExecQueueByName(@PathVariable("queueName") String queueName);

    @GetMapping("/api/engine-execution-queues/all")
    List<EngineExecutionQueue> getExecQueueList(@SpringQueryMap EngineExecutionQueueQueryReq req);

    // endregion

    // region application

    @GetMapping("/api/applications/{appName}")
    ApplicationDetailResp getByAppName(@PathVariable("appName") String appName);

    // endregion
}
