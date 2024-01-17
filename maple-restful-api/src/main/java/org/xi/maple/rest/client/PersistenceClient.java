package org.xi.maple.rest.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.MapleEngineExecutionQueue;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.rest.client.fallback.PersistenceClientFallbackFactory;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.List;

@FeignClient(value = MapleServiceName.PERSISTENCE, fallbackFactory = PersistenceClientFallbackFactory.class, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.PERSISTENCE, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface PersistenceClient {

    @PostMapping("/engine-execution/add")
    Integer addExecution(@RequestBody EngineExecutionAddRequest engineExecution);

    @GetMapping("/engine-execution/detail")
    EngineExecutionDetailResponse getExecutionById(@RequestParam("id") Integer id);

    @PatchMapping("/engine-execution/update-status/{id}")
    Integer updateExecutionStatusById(@PathVariable("id") Integer id, @Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionInfoById(@RequestParam("id")Integer id, @RequestBody EngineExecutionUpdateRequest updateRequest);


    @PostMapping("/engine-execution-queue/add-or-update")
    OperateResult<Integer> addOrUpdateExecQueue(@RequestBody MapleEngineExecutionQueue engineExecutionQueue);

    @DeleteMapping("/engine-execution-queue/delete")
    Integer deleteExecQueue(@RequestParam("queueName") String queueName);

    @GetMapping("/engine-execution-queue/detail")
    EngineExecutionQueue getExecQueueByName(@RequestParam("queueName") String queueName);

    @GetMapping("/engine-execution-queue/list")
    List<EngineExecutionQueue> getExecQueueList(@SpringQueryMap EngineExecutionQueueQueryRequest queryRequest);

    @GetMapping("/application/detail")
    ApplicationDetailResponse getByAppName(@RequestParam("appName") String appName);
}
