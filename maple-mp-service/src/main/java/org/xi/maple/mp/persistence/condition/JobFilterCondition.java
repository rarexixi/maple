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
     * 用户组
     */
    private Integer userGroup;

    /**
     * 用户组列表
     */
    private Collection<Integer> userGroupIn;

    /**
     * 排除的用户组列表
     */
    private Collection<Integer> userGroupNotIn;

    /**
     * 最小用户组
     */
    private Integer userGroupMin;

    /**
     * 最大用户组
     */
    private Integer userGroupMax;

    /**
     * 作业负责人
     */
    private Integer owner;

    /**
     * 作业负责人列表
     */
    private Collection<Integer> ownerIn;

    /**
     * 排除的作业负责人列表
     */
    private Collection<Integer> ownerNotIn;

    /**
     * 最小作业负责人
     */
    private Integer ownerMin;

    /**
     * 最大作业负责人
     */
    private Integer ownerMax;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 来源应用列表
     */
    private Collection<String> fromAppIn;

    /**
     * 排除的来源应用列表
     */
    private Collection<String> fromAppNotIn;

    /**
     * 来源应用不为空
     */
    private Boolean fromAppIsNotEmpty;

    /**
     * 来源应用为空
     */
    private Boolean fromAppIsEmpty;

    /**
     * 来源应用开始
     */
    private String fromAppStartWith;

    /**
     * 来源应用结束
     */
    private String fromAppEndWith;

    /**
     * 来源应用包含
     */
    private String fromAppContains;

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
     * 所属集群
     */
    private Integer clusterId;

    /**
     * 所属集群列表
     */
    private Collection<Integer> clusterIdIn;

    /**
     * 排除的所属集群列表
     */
    private Collection<Integer> clusterIdNotIn;

    /**
     * 最小所属集群
     */
    private Integer clusterIdMin;

    /**
     * 最大所属集群
     */
    private Integer clusterIdMax;

    /**
     * 集群类型
     */
    private String clusterCategory;

    /**
     * 集群类型列表
     */
    private Collection<String> clusterCategoryIn;

    /**
     * 排除的集群类型列表
     */
    private Collection<String> clusterCategoryNotIn;

    /**
     * 集群类型不为空
     */
    private Boolean clusterCategoryIsNotEmpty;

    /**
     * 集群类型为空
     */
    private Boolean clusterCategoryIsEmpty;

    /**
     * 集群类型开始
     */
    private String clusterCategoryStartWith;

    /**
     * 集群类型结束
     */
    private String clusterCategoryEndWith;

    /**
     * 集群类型包含
     */
    private String clusterCategoryContains;

    /**
     * 作业优先级
     */
    private Integer priority;

    /**
     * 作业优先级列表
     */
    private Collection<Integer> priorityIn;

    /**
     * 排除的作业优先级列表
     */
    private Collection<Integer> priorityNotIn;

    /**
     * 最小作业优先级
     */
    private Integer priorityMin;

    /**
     * 最大作业优先级
     */
    private Integer priorityMax;

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
