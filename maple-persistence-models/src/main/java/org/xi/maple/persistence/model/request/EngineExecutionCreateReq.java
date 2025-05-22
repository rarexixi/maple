package org.xi.maple.persistence.model.request;

import lombok.Data;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

@Data
public class EngineExecutionCreateReq extends BaseEntity {

    /**
     * 执行文件
     */
    @NotBlank(message = "execFile(执行文件)不能为空")
    private String execFile;

    /**
     * 作业ID
     */
    @NotNull(message = "jobId(作业ID)不能为空")
    private Integer jobId;

    /**
     * 来源应用
     */
    @NotBlank(message = "fromApp(来源应用)不能为空")
    private String fromApp;

    /**
     * 执行批次ID
     */
    @NotBlank(message = "bizId(执行批次ID)不能为空")
    private String bizId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 作业类型
     */
    @NotBlank(message = "jobType(作业类型)不能为空")
    private String jobType;

    /**
     * 引擎ID
     */
    @NotNull(message = "engineId(引擎ID)不能为空")
    private Integer engineId;

    /**
     * 集群资源组名称
     */
    @NotBlank(message = "resourceGroupKeys(集群资源组名称)不能为空")
    private String resourceGroupKeys;

    /**
     * 集群资源组
     */
    @NotBlank(message = "resourceGroupValues(集群资源组)不能为空")
    private String resourceGroupValues;

    /**
     * 初始优先级
     */
    @NotNull(message = "priority(初始优先级)不能为空")
    private Integer priority;

    /**
     * 优先级可提升
     */
    private Boolean priUpgradable;

    /**
     * 用户组
     */
    @NotNull(message = "userGroup(用户组)不能为空")
    private Integer userGroup;

    /**
     * 执行人
     */
    @NotNull(message = "runBy(执行人)不能为空")
    private Integer runBy;

    /**
     * 集群应用ID
     */
    private String clusterAppId;

    /**
     * 集群应用地址
     */
    private String clusterAppAddress;

    /**
     * 作业配置
     */
    private String execConf;

    /**
     * 启动参数信息
     */
    private String runConf;
}
