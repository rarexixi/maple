package org.xi.maple.persistence.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EngineExecutionListItemResponse implements Serializable {

    /**
     * 执行ID
     */
    private Integer id;

    /**
     * 执行文件
     */
    private String execFile;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 作业ID
     */
    private String jobId;

    /**
     * 执行批次ID
     */
    private String bizId;

    /**
     * 应用作业执行唯一ID
     */
    private String execUniqId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 提交集群
     */
    private String cluster;

    /**
     * 集群资源组
     */
    private String resourceGroup;

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
    private Integer runPri;

    /**
     * 优先级可提升
     */
    private Boolean priUpgradable;

    /**
     * 用户组
     */
    private String group;

    /**
     * 用户
     */
    private String user;

    /**
     * 集群应用ID
     */
    private String clusterAppId;

    /**
     * 状态
     */
    private String status;

    /**
     * 任务提交时间
     */
    private LocalDateTime startingTime;

    /**
     * 任务执行开始时间
     */
    private LocalDateTime runningTime;

    /**
     * 任务执行结束时间
     */
    private LocalDateTime finishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
