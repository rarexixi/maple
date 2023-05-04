package org.xi.maple.scheduler.persistence.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 执行作业实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Data
public class JobEntity {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 作业唯一标识
     */
    private String uniqueId;

    /**
     * 作业说明
     */
    private String jobComment;

    /**
     * 引擎ID
     */
    private Integer engineId;

    /**
     * 引擎类型
     */
    private String engineCategory;

    /**
     * 版本
     */
    private String engineVersion;

    /**
     * 集群
     */
    private String cluster;

    /**
     * 提交队列
     */
    private String queue;

    /**
     * 初始优先级
     */
    private Integer priority;

    /**
     * 运行优先级
     */
    private Integer runPriority;

    /**
     * 作业类型 (sync, async)
     */
    private String jobType;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行结果类型 (text, path)
     */
    private String resultType;

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
     * 回调地址
     */
    private String webhooks;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
