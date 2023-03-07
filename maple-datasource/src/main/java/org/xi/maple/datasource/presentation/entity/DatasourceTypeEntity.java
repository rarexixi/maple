package org.xi.maple.datasource.presentation.entity;

import org.xi.maple.datasource.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

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
}
