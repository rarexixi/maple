package org.xi.maple.scheduler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.scheduler.client.fallback.PersistenceClientFallbackFactory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "maple-persistence-service", fallbackFactory = PersistenceClientFallbackFactory.class)
public interface PersistenceClient {

    // region engine-execution

    @PostMapping("/engine-execution/add")
    Integer addExecution(@Validated @RequestBody EngineExecutionAddRequest engineExecution);

    @GetMapping("/engine-execution/detail")
    EngineExecutionDetailResponse getExecutionById(@RequestParam("id") @NotNull(message = "执行ID不能为空") @Min(value = 1, message = "执行ID必须大于0") Integer id);

    @PatchMapping("/engine-execution/update-status")
    Integer updateExecutionStatusById(@Validated @RequestBody EngineExecutionUpdateStatusRequest updateStatusRequest);

    @PatchMapping("/engine-execution/update-ext-info")
    Integer updateExecutionExtInfoById(@Validated @RequestBody EngineExecutionUpdateRequest updateRequest);

    // endregion

    // region engine-execution-queue

    @PostMapping("/engine-execution-queue/add-or-update")
    OperateResult<Integer> addOrUpdateExecQueue(@Validated @RequestBody EngineExecutionQueueSaveRequest engineExecutionQueue);

    @DeleteMapping("/engine-execution-queue/delete")
    Integer deleteExecQueue(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/engine-execution-queue/detail")
    EngineExecutionQueue getExecQueueByName(@RequestParam("queueName") @NotBlank(message = "执行队列名不能为空") String queueName);

    @GetMapping("/engine-execution-queue/list")
    List<EngineExecutionQueue> getExecQueueList(@SpringQueryMap EngineExecutionQueueQueryRequest queryRequest);

    // endregion

    // region cluster

    @GetMapping("/cluster/detail")
    ClusterDetailResponse getByName(@RequestParam("name") @NotBlank(message = "集群名称不能为空") String name);

    @GetMapping("/cluster/list")
    List<ClusterListItemResponse> getClusterList(@SpringQueryMap ClusterQueryRequest queryRequest);

    // endregion
}
