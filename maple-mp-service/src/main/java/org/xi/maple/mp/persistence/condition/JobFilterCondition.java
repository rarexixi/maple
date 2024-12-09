package org.xi.maple.mp.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 执行作业查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class JobFilterCondition implements FilterCondition {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业ID列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的作业ID列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小作业ID
     */
    private Integer idMin;

    /**
     * 最大作业ID
     */
    private Integer idMax;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业名列表
     */
    private Collection<String> jobNameIn;

    /**
     * 排除的作业名列表
     */
    private Collection<String> jobNameNotIn;

    /**
     * 作业名不为空
     */
    private Boolean jobNameIsNotEmpty;

    /**
     * 作业名为空
     */
    private Boolean jobNameIsEmpty;

    /**
     * 作业名开始
     */
    private String jobNameStartWith;

    /**
     * 作业名结束
     */
    private String jobNameEndWith;

    /**
     * 作业名包含
     */
    private String jobNameContains;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 作业类型列表
     */
    private Collection<String> jobTypeIn;

    /**
     * 排除的作业类型列表
     */
    private Collection<String> jobTypeNotIn;

    /**
     * 作业类型不为空
     */
    private Boolean jobTypeIsNotEmpty;

    /**
     * 作业类型为空
     */
    private Boolean jobTypeIsEmpty;

    /**
     * 作业类型开始
     */
    private String jobTypeStartWith;

    /**
     * 作业类型结束
     */
    private String jobTypeEndWith;

    /**
     * 作业类型包含
     */
    private String jobTypeContains;

    /**
     * 引擎ID
     */
    private Integer engineId;

    /**
     * 引擎ID列表
     */
    private Collection<Integer> engineIdIn;

    /**
     * 排除的引擎ID列表
     */
    private Collection<Integer> engineIdNotIn;

    /**
     * 最小引擎ID
     */
    private Integer engineIdMin;

    /**
     * 最大引擎ID
     */
    private Integer engineIdMax;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 作业负责人列表
     */
    private Collection<String> ownerIn;

    /**
     * 排除的作业负责人列表
     */
    private Collection<String> ownerNotIn;

    /**
     * 作业负责人不为空
     */
    private Boolean ownerIsNotEmpty;

    /**
     * 作业负责人为空
     */
    private Boolean ownerIsEmpty;

    /**
     * 作业负责人开始
     */
    private String ownerStartWith;

    /**
     * 作业负责人结束
     */
    private String ownerEndWith;

    /**
     * 作业负责人包含
     */
    private String ownerContains;

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
