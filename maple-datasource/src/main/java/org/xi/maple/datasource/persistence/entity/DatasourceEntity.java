package org.xi.maple.datasource.persistence.entity;

import org.xi.maple.datasource.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源配置实体
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
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型
     */
    private String datasourceType;

    /**
     * 数据源版本
     */
    private String version;

    /**
     * 配置JSON
     */
    private String datasourceConfig;
}
