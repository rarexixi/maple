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
 * 数据源类型查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceTypeFilterCondition implements FilterCondition {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型编码列表
     */
    private Collection<String> typeCodeIn;

    /**
     * 排除的类型编码列表
     */
    private Collection<String> typeCodeNotIn;

    /**
     * 类型编码不为空
     */
    private Boolean typeCodeIsNotEmpty;

    /**
     * 类型编码为空
     */
    private Boolean typeCodeIsEmpty;

    /**
     * 类型编码开始
     */
    private String typeCodeStartWith;

    /**
     * 类型编码结束
     */
    private String typeCodeEndWith;

    /**
     * 类型编码包含
     */
    private String typeCodeContains;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 类型名称列表
     */
    private Collection<String> typeNameIn;

    /**
     * 排除的类型名称列表
     */
    private Collection<String> typeNameNotIn;

    /**
     * 类型名称不为空
     */
    private Boolean typeNameIsNotEmpty;

    /**
     * 类型名称为空
     */
    private Boolean typeNameIsEmpty;

    /**
     * 类型名称开始
     */
    private String typeNameStartWith;

    /**
     * 类型名称结束
     */
    private String typeNameEndWith;

    /**
     * 类型名称包含
     */
    private String typeNameContains;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 图标地址列表
     */
    private Collection<String> iconIn;

    /**
     * 排除的图标地址列表
     */
    private Collection<String> iconNotIn;

    /**
     * 图标地址不为空
     */
    private Boolean iconIsNotEmpty;

    /**
     * 图标地址为空
     */
    private Boolean iconIsEmpty;

    /**
     * 图标地址开始
     */
    private String iconStartWith;

    /**
     * 图标地址结束
     */
    private String iconEndWith;

    /**
     * 图标地址包含
     */
    private String iconContains;

    /**
     * 分类
     */
    private String classifier;

    /**
     * 分类列表
     */
    private Collection<String> classifierIn;

    /**
     * 排除的分类列表
     */
    private Collection<String> classifierNotIn;

    /**
     * 分类不为空
     */
    private Boolean classifierIsNotEmpty;

    /**
     * 分类为空
     */
    private Boolean classifierIsEmpty;

    /**
     * 分类开始
     */
    private String classifierStartWith;

    /**
     * 分类结束
     */
    private String classifierEndWith;

    /**
     * 分类包含
     */
    private String classifierContains;

    /**
     * 版本(多个版本用","隔开)
     */
    private String versions;

    /**
     * 版本列表
     */
    private Collection<String> versionsIn;

    /**
     * 排除的版本列表
     */
    private Collection<String> versionsNotIn;

    /**
     * 版本不为空
     */
    private Boolean versionsIsNotEmpty;

    /**
     * 版本为空
     */
    private Boolean versionsIsEmpty;

    /**
     * 版本开始
     */
    private String versionsStartWith;

    /**
     * 版本结束
     */
    private String versionsEndWith;

    /**
     * 版本包含
     */
    private String versionsContains;

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
