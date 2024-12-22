package org.xi.maple.mp.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceFilterCondition implements FilterCondition {

    /**
     * Id
     */
    private Integer id;

    /**
     * Id列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的Id列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小Id
     */
    private Integer idMin;

    /**
     * 最大Id
     */
    private Integer idMax;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源名称列表
     */
    private Collection<String> nameIn;

    /**
     * 排除的数据源名称列表
     */
    private Collection<String> nameNotIn;

    /**
     * 数据源名称不为空
     */
    private Boolean nameIsNotEmpty;

    /**
     * 数据源名称为空
     */
    private Boolean nameIsEmpty;

    /**
     * 数据源名称开始
     */
    private String nameStartWith;

    /**
     * 数据源名称结束
     */
    private String nameEndWith;

    /**
     * 数据源名称包含
     */
    private String nameContains;

    /**
     * 数据源描述
     */
    private String description;


    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 数据源类型列表
     */
    private Collection<String> datasourceTypeIn;

    /**
     * 排除的数据源类型列表
     */
    private Collection<String> datasourceTypeNotIn;

    /**
     * 数据源类型不为空
     */
    private Boolean datasourceTypeIsNotEmpty;

    /**
     * 数据源类型为空
     */
    private Boolean datasourceTypeIsEmpty;

    /**
     * 数据源类型开始
     */
    private String datasourceTypeStartWith;

    /**
     * 数据源类型结束
     */
    private String datasourceTypeEndWith;

    /**
     * 数据源类型包含
     */
    private String datasourceTypeContains;

    /**
     * 数据源版本
     */
    private String version;

    /**
     * 数据源版本列表
     */
    private Collection<String> versionIn;

    /**
     * 排除的数据源版本列表
     */
    private Collection<String> versionNotIn;

    /**
     * 数据源版本不为空
     */
    private Boolean versionIsNotEmpty;

    /**
     * 数据源版本为空
     */
    private Boolean versionIsEmpty;

    /**
     * 数据源版本开始
     */
    private String versionStartWith;

    /**
     * 数据源版本结束
     */
    private String versionEndWith;

    /**
     * 数据源版本包含
     */
    private String versionContains;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 创建人列表
     */
    private Collection<Integer> createdByIn;

    /**
     * 排除的创建人列表
     */
    private Collection<Integer> createdByNotIn;

    /**
     * 最小创建人
     */
    private Integer createdByMin;

    /**
     * 最大创建人
     */
    private Integer createdByMax;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 修改人列表
     */
    private Collection<Integer> updatedByIn;

    /**
     * 排除的修改人列表
     */
    private Collection<Integer> updatedByNotIn;

    /**
     * 最小修改人
     */
    private Integer updatedByMin;

    /**
     * 最大修改人
     */
    private Integer updatedByMax;

    /**
     * 最小创建时间
     */
    private LocalDateTime createdAtMin;

    /**
     * 最大创建时间
     */
    private LocalDateTime createdAtMax;

    /**
     * 最小更新时间
     */
    private LocalDateTime updatedAtMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime updatedAtMax;
}
