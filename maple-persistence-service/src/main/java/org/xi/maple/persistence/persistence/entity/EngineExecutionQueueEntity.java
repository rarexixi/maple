package org.xi.maple.persistence.persistence.entity;

import org.xi.maple.persistence.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行队列实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionQueueEntity extends BaseEntity {

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
    private Integer priority;
}
