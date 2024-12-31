package org.xi.maple.rest.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExecReq extends BaseEntity {

    /**
     * 执行ID
     */
    @NotNull(message = "id(执行ID)不能为空")
    private Integer id;

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
     * 作业配置
     */
    private String configuration;
}
