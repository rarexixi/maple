package org.xi.maple.mp.persistence.entity;

import org.xi.maple.common.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源类型实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceTypeEntity extends BaseEntity {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 分类
     */
    private String classifier;

    /**
     * 版本(多个版本用","隔开)
     */
    private String versions;

    /**
     * 数据源配置信息
     */
    private String configurations;
}
