package org.xi.maple.persistence.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EngineExecutionQueue implements Serializable {

    /**
     * 执行队列名
     */
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
