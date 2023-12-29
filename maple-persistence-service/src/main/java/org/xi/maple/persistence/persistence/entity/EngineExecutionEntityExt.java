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
     * 集群类型
     */
    private String clusterCategory;

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
    private String execInfo;
}
