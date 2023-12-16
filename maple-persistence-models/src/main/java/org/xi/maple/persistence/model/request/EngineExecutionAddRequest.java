package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class EngineExecutionAddRequest extends BaseEntity {

    /**
     * 执行标识
     */
    @NotBlank(message = "uniqueId(执行标识)不能为空")
    private String uniqueId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 作业说明
     */
    private String execComment;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行内容路径
     */
    private String contentPath;

    /**
     * 来源应用
     */
    @NotBlank(message = "fromApp(来源应用)不能为空")
    private String fromApp;

    /**
     * 提交集群
     */
    @NotBlank(message = "cluster(提交集群)不能为空")
    private String cluster;

    /**
     * 集群队列
     */
    private String clusterQueue;

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
     * 初始优先级
     */
    @NotNull(message = "priority(初始优先级)不能为空")
    private Integer priority2;

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
     * 执行内容
     */
    private String execContent;

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
    private String processInfo;
}
