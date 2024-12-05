package org.xi.maple.mp.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 系统配置查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class SysConfFilterCondition implements FilterCondition {

    /**
     * 配置键
     */
    private String confKey;

    /**
     * 配置键列表
     */
    private Collection<String> confKeyIn;

    /**
     * 排除的配置键列表
     */
    private Collection<String> confKeyNotIn;

    /**
     * 配置键不为空
     */
    private Boolean confKeyIsNotEmpty;

    /**
     * 配置键为空
     */
    private Boolean confKeyIsEmpty;

    /**
     * 配置键开始
     */
    private String confKeyStartWith;

    /**
     * 配置键结束
     */
    private String confKeyEndWith;

    /**
     * 配置键包含
     */
    private String confKeyContains;

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
