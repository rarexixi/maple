package org.xi.maple.mp.persistence.entity;

import org.xi.maple.common.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceEntity extends BaseEntity {

    /**
     * Id
     */
    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源描述
     */
    private String description;

    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 数据源版本
     */
    private String version;

    /**
     * 数据源配置
     */
    private String datasourceConf;
}
