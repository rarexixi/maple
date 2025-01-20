package org.xi.maple.persistence.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobDetailResp implements Serializable {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业说明
     */
    private String description;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 用户组
     */
    private Integer userGroup;

    /**
     * 作业负责人
     */
    private Integer owner;

    /**
     * 来源应用
     */
    private String fromApp;

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
     * 作业优先级
     */
    private Integer priority;

    /**
     * 优先级可提升
     */
    private Boolean priUpgradable;

    /**
     * 执行配置
     */
    private String runConf;

    /**
     * 作业配置
     */
    private String jobConf;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;
}
