package org.xi.maple.persistence.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 引擎执行记录扩展实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionEntityExt extends EngineExecutionEntity {
    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 集群类型
     */
    private String clusterName;

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
}
