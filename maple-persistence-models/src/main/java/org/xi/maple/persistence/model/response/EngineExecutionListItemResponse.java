package org.xi.maple.persistence.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EngineExecutionListItemResponse implements Serializable {

    /**
     * 执行ID
     */
    private Integer id;

    /**
     * 执行标识
     */
    private String uniqueId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 作业说明
     */
    private String execComment;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行内容路径
     */
    private String contentPath;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 提交集群
     */
    private String cluster;

    /**
     * 集群队列
     */
    private String clusterQueue;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 初始优先级
     */
    private Integer priority;

    /**
     * 运行优先级
     */
    private Integer runPriority;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, SUCCEED, FAILED, KILLED)
     */
    private String status;

    /**
     * 用户组
     */
    private String group;

    /**
     * 用户
     */
    private String user;

    /**
     * 创建时间
     */
    private LocalDateTime startTime;

    /**
     * 停止时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime heartbeatTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
