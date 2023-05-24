package org.xi.maple.api.persistence.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class JobQueueEntity implements Serializable {

    /**
     * 作业队列名
     */
    private String queueName;

    /**
     * 作业队列锁名
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
     * 引擎分类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 引擎类型 (once，resident)
     */
    private String engineType;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 用户组
     */
    private String group;

    /**
     * 作业优先级
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}