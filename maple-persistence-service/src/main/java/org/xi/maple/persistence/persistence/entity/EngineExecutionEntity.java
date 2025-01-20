package org.xi.maple.persistence.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;

import java.time.LocalDateTime;

/**
 * 引擎执行记录实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionEntity extends BaseEntity {

    /**
     * 执行ID
     */
    private Integer id;

    /**
     * 执行文件
     */
    private String execFile;

    /**
     * 作业ID
     */
    private Integer jobId;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 执行批次ID
     */
    private String bizId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 引擎ID
     */
    private Integer engineId;

    /**
     * 所属集群
     */
    private Integer clusterId;

    /**
     * 集群类型
     */
    private String clusterCategory;

    /**
     * 集群资源组
     */
    private String resourceGroup;

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
    private Integer userGroup;

    /**
     * 执行人
     */
    private Integer runBy;

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
    private LocalDateTime submittedAt;

    /**
     * 任务执行开始时间
     */
    private LocalDateTime startedAt;

    /**
     * 任务执行结束时间
     */
    private LocalDateTime finishedAt;
}
