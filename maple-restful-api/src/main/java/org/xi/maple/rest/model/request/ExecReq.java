package org.xi.maple.rest.model.request;

import lombok.Data;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ExecReq extends BaseEntity {

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
     * 作业配置
     */
    @NotBlank(message = "execConf(作业配置)不能为空")
    private String execConf;

    /**
     * 启动参数信息
     */
    @NotBlank(message = "runConf(启动参数信息)不能为空")
    private String runConf;

    private void setExecConf(Map<String, ?> execConf) {
        this.execConf = JsonUtils.toJsonString(execConf, "");
    }

    private void setRunConf(Map<String, ?> runConf) {
        this.runConf = JsonUtils.toJsonString(runConf, "");
    }

    private void setResourceGroup(Map<String, String> runConf) {
        final List<String> resourceGroupKeys = new ArrayList<>(runConf.size());
        final List<String> resourceGroupValues = new ArrayList<>(runConf.size());
        runConf.forEach((k, v) -> {
            resourceGroupKeys.add(k);
            resourceGroupValues.add(v);
        });
        this.resourceGroupKeys = String.join("--", resourceGroupKeys);
        this.resourceGroupValues = String.join("--", resourceGroupValues);
    }
}
