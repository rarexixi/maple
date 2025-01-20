package org.xi.maple.persistence.model.response;

import lombok.Data;

@Data
public class EngineExecutionDetailResp extends EngineExecutionItemResp {
    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 集群类型
     */
    private String clusterName;

    /**
     * 集群类型
     */
    private String clusterCategory;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 作业配置
     */
    private String execConf;

    /**
     * 启动参数信息
     */
    private String runConf;

    /**
     * 执行信息
     */
    private String execInfo;

    /**
     * 任务启动人
     */
    private String runByName;

    /**
     * 任务启动用户组名
     */
    private String userGroupName;
}
