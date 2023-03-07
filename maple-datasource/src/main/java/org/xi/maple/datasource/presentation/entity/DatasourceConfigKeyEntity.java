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
 * 数据源配置项实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceConfigKeyEntity extends BaseEntity {

    /**
     * Id
     */
    private Integer id;

    /**
     * 类型
     */
    private String datasourceType;

    /**
     * 版本
     */
    private String versions;

    /**
     * 配置编码
     */
    private String keyCode;

    /**
     * 配置名
     */
    private String keyName;

    /**
     * 配置顺序
     */
    private Integer keyOrder;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 类型
     */
    private String valueType;

    /**
     * 是否必填
     */
    private Integer required;

    /**
     * 校验正则
     */
    private String valueRegex;

    /**
     * 配置说明
     */
    private String description;
}
