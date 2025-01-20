package org.xi.maple.persistence.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;


/**
 * 引擎执行扩展信息实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionExtInfoEntity extends BaseEntity {

    /**
     * 执行ID
     */
    private Integer id;

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
