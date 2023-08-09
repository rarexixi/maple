package org.xi.maple.persistence.persistence.entity;

import java.math.BigDecimal;
import java.util.Collection;

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
