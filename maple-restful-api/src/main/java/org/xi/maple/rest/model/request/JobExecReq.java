package org.xi.maple.rest.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class JobExecReq implements Serializable {

    /**
     * 执行文件
     */
    @NotBlank(message = "execFile(执行文件)不能为空")
    private String execFile;

    /**
     * 来源应用
     */
    @NotBlank(message = "fromApp(来源应用)不能为空")
    private String fromApp;

    /**
     * 作业ID
     */
    @NotBlank(message = "jobId(作业ID)不能为空")
    private String jobId;

    /**
     * 执行批次ID
     */
    @NotBlank(message = "bizId(执行批次ID)不能为空")
    private String bizId;

    /**
     * 应用作业执行唯一ID
     */
    @NotBlank(message = "execUniqId(应用作业执行唯一ID)不能为空")
    private String execUniqId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 提交集群
     */
    @NotBlank(message = "cluster(提交集群)不能为空")
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
    @NotNull(message = "priority(初始优先级)不能为空")
    private Integer priority;

    /**
     * 运行优先级
     */
    @NotNull(message = "runPri(运行优先级)不能为空")
    private Integer runPri;

    /**
     * 优先级可提升
     */
    private Boolean priUpgradable = false;

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
    private LocalDateTime submittedAt;

    /**
     * 任务执行开始时间
     */
    private LocalDateTime startedAt;

    /**
     * 任务执行结束时间
     */
    private LocalDateTime finishedAt;

    /**
     * 作业配置
     */
    private String configuration;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 执行信息
     */
    private String execInfo;
}
