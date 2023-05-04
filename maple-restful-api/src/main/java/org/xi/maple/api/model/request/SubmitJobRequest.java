package org.xi.maple.api.model.request;

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
     * 引擎类型
     */
    private String engineType;

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
     * 优先级
     */
    private Integer priority;

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
