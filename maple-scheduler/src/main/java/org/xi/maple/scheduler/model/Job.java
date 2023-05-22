package org.xi.maple.scheduler.model;

import lombok.Data;

/**
 * @author xishihao
 */
@Data
public class Job {

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
    private String engineType;

    /**
     * 版本
     */
    private String engineVersion;

    /**
     * 提交集群
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
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED)
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
}
