package org.xi.maple.persistence.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class EngineExecutionItemResp implements Serializable {

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
     * 作业类型
     */
    private String jobType;

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
     * 集群资源组名称
     */
    private String resourceGroupKeys;

    /**
     * 集群资源组
     */
    private String resourceGroupValues;

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
     * 集群应用地址
     */
    private String clusterAppAddress;

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
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @JsonIgnore
    public Map<String, String> getResourceGroup() {
        String[] groupKeys = resourceGroupKeys.split("--");
        String[] groupValues = resourceGroupValues.split("--");
        if (groupKeys.length != groupValues.length) {
            return null;
        }
        Map<String, String> resourceGroup = new HashMap<>();
        for (int i = 0; i < groupKeys.length; i++) {
            resourceGroup.put(groupKeys[i], groupValues[i]);
        }
        return resourceGroup;
    }
}
