package org.xi.maple.api.persistence.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class JobQueueEntity implements Serializable {

    /**
     * QueueName
     */
    private String queueName;

    /**
     * LockName
     */
    private String lockName;

    /**
     * Cluster
     */
    private String cluster;

    /**
     * ClusterQueue
     */
    private String clusterQueue;

    /**
     * Type
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}