package org.xi.maple.datacalc.api.model.request;

import lombok.Data;

/**
 * 作业提交请求对象
 *
 * @author xishihao
 */
@Data
public class SubmitJobRequest {

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业类型 (sync, async, once)
     */
    private String jobType;

    /**
     * 作业唯一标识
     */
    private String uniqueId;

    /**
     * 作业说明
     */
    private String jobComment;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 提交集群
     */
    private String cluster;

    /**
     * 集群队列
     */
    private String clusterQueue;

    /**
     * 引擎分类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 初始优先级
     */
    private Integer priority;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行结果类型 (text, path)
     */
    private String resultType;

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
     * 作业配置
     */
    private String configuration;

    /**
     * 扩展信息
     */
    private String extInfo;
}
