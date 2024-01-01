package org.xi.maple.persistence.persistence.entity;

import org.xi.maple.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 集群引擎默认配置实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineDefaultConfEntity extends BaseEntity {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 主体类型
     */
    private String objType;

    /**
     * 所属主体
     */
    private String objName;

    /**
     * 集群引擎ID
     */
    private Integer engineId;

    /**
     * 默认配置
     */
    private String defaultConf;
}
