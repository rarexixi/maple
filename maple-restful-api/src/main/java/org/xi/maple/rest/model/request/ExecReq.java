package org.xi.maple.rest.model.request;

import lombok.Data;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExecReq extends BaseEntity {

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
     * 引擎ID
     */
    @NotNull(message = "engineId(引擎ID)不能为空")
    private Integer engineId;

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
     * 作业配置
     */
    @NotBlank(message = "execConf(作业配置)不能为空")
    private String execConf;

    /**
     * 启动参数信息
     */
    @NotBlank(message = "runConf(启动参数信息)不能为空")
    private String runConf;
}
