package org.xi.maple.persistence.model.request;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class EngineExecutionQueueSaveReq extends BaseEntity {

    /**
     * 执行队列名
     */
    @NotBlank(message = "queueName(执行队列名)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String queueName;

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
    @NotNull(message = "priority(队列优先级)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private Integer priority;
}
