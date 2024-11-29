package org.xi.maple.manager.client;

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

    @PostMapping("/engine-executions")
    Integer addExecution(@RequestBody EngineExecutionSaveReq req);

    @GetMapping("/engine-executions/{id}")
    EngineExecutionDetailResp getExecutionById(@PathVariable("id") Integer id);

    @PatchMapping("/engine-executions/{id}/status")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @RequestBody EngineExecutionStatusUpdateReq req);

    @PatchMapping("/engine-executions/{id}/ext-info")
    Integer updateExecutionExtInfoById(@PathVariable("id") Integer id, @RequestBody EngineExecutionExtUpdateReq req);

    // endregion

    // region engine-execution-queue

    @PostMapping("/engine-execution-queues")
    OperateResult<Integer> upsertExecQueue(@RequestBody EngineExecutionQueueSaveReq req);

    @DeleteMapping("/engine-execution-queues/{queueName}")
    Integer deleteExecQueue(@PathVariable("queueName") String queueName);

    @GetMapping("/engine-execution-queues/{queueName}")
    EngineExecutionQueue getExecQueueByName(@PathVariable("queueName") String queueName);

    @GetMapping("/engine-execution-queues/all")
    List<EngineExecutionQueue> getExecQueueList(@SpringQueryMap EngineExecutionQueueQueryReq req);

    // endregion

    // region cluster

    @GetMapping("/clusters/{name}")
    ClusterDetailResp getClusterByName(@PathVariable("name") String name);

    @GetMapping("/clusters/all")
    List<ClusterItemResp> getClusterList(@SpringQueryMap ClusterQueryReq queryReq);

    // endregion
}
