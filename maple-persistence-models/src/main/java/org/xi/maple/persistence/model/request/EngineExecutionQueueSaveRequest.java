package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class EngineExecutionQueueSaveRequest extends BaseEntity {

    /**
     * 执行队列名
     */
    private String queueName;

    /**
     * 执行队列锁名
     */
    private String lockName;

    /**
     * 提交集群
     */
    private String cluster;

    /**
     * 集群队列
     */
    private String clusterQueue;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 用户组
     */
    private String group;

    /**
     * 队列优先级
     */
    @NotNull(message = "priority(队列优先级)不能为空")
    private Integer priority;
}
