package org.xi.maple.persistence.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.model.SelectCondition;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 引擎执行记录查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionSelectCondition extends SelectCondition {

    /**
     * 执行ID
     */
    private Integer id;

    /**
     * 执行ID列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的执行ID列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小执行ID
     */
    private Integer idMin;

    /**
     * 最大执行ID
     */
    private Integer idMax;

    /**
     * 执行文件
     */
    private String execFile;

    /**
     * 执行文件列表
     */
    private Collection<String> execFileIn;

    /**
     * 排除的执行文件列表
     */
    private Collection<String> execFileNotIn;

    /**
     * 执行文件不为空
     */
    private Boolean execFileIsNotEmpty;

    /**
     * 执行文件为空
     */
    private Boolean execFileIsEmpty;

    /**
     * 执行文件开始
     */
    private String execFileStartWith;

    /**
     * 执行文件结束
     */
    private String execFileEndWith;

    /**
     * 执行文件包含
     */
    private String execFileContains;

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
     * 作业ID
     */
    private String jobId;

    /**
     * 作业ID列表
     */
    private Collection<String> jobIdIn;

    /**
     * 排除的作业ID列表
     */
    private Collection<String> jobIdNotIn;

    /**
     * 作业ID不为空
     */
    private Boolean jobIdIsNotEmpty;

    /**
     * 作业ID为空
     */
    private Boolean jobIdIsEmpty;

    /**
     * 作业ID开始
     */
    private String jobIdStartWith;

    /**
     * 作业ID结束
     */
    private String jobIdEndWith;

    /**
     * 作业ID包含
     */
    private String jobIdContains;

    /**
     * 执行批次ID
     */
    private String bizId;

    /**
     * 执行批次ID列表
     */
    private Collection<String> bizIdIn;

    /**
     * 排除的执行批次ID列表
     */
    private Collection<String> bizIdNotIn;

    /**
     * 执行批次ID不为空
     */
    private Boolean bizIdIsNotEmpty;

    /**
     * 执行批次ID为空
     */
    private Boolean bizIdIsEmpty;

    /**
     * 执行批次ID开始
     */
    private String bizIdStartWith;

    /**
     * 执行批次ID结束
     */
    private String bizIdEndWith;

    /**
     * 执行批次ID包含
     */
    private String bizIdContains;

    /**
     * 应用作业执行唯一ID
     */
    private String execUniqId;

    /**
     * 应用作业执行唯一ID列表
     */
    private Collection<String> execUniqIdIn;

    /**
     * 排除的应用作业执行唯一ID列表
     */
    private Collection<String> execUniqIdNotIn;

    /**
     * 应用作业执行唯一ID不为空
     */
    private Boolean execUniqIdIsNotEmpty;

    /**
     * 应用作业执行唯一ID为空
     */
    private Boolean execUniqIdIsEmpty;

    /**
     * 应用作业执行唯一ID开始
     */
    private String execUniqIdStartWith;

    /**
     * 应用作业执行唯一ID结束
     */
    private String execUniqIdEndWith;

    /**
     * 应用作业执行唯一ID包含
     */
    private String execUniqIdContains;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 执行名称列表
     */
    private Collection<String> execNameIn;

    /**
     * 排除的执行名称列表
     */
    private Collection<String> execNameNotIn;

    /**
     * 执行名称不为空
     */
    private Boolean execNameIsNotEmpty;

    /**
     * 执行名称为空
     */
    private Boolean execNameIsEmpty;

    /**
     * 执行名称开始
     */
    private String execNameStartWith;

    /**
     * 执行名称结束
     */
    private String execNameEndWith;

    /**
     * 执行名称包含
     */
    private String execNameContains;

    /**
     * 提交集群
     */
    private String cluster;

    /**
     * 提交集群列表
     */
    private Collection<String> clusterIn;

    /**
     * 排除的提交集群列表
     */
    private Collection<String> clusterNotIn;

    /**
     * 提交集群不为空
     */
    private Boolean clusterIsNotEmpty;

    /**
     * 提交集群为空
     */
    private Boolean clusterIsEmpty;

    /**
     * 提交集群开始
     */
    private String clusterStartWith;

    /**
     * 提交集群结束
     */
    private String clusterEndWith;

    /**
     * 提交集群包含
     */
    private String clusterContains;

    /**
     * 集群资源组
     */
    private String resourceGroup;

    /**
     * 集群资源组列表
     */
    private Collection<String> resourceGroupIn;

    /**
     * 排除的集群资源组列表
     */
    private Collection<String> resourceGroupNotIn;

    /**
     * 集群资源组不为空
     */
    private Boolean resourceGroupIsNotEmpty;

    /**
     * 集群资源组为空
     */
    private Boolean resourceGroupIsEmpty;

    /**
     * 集群资源组开始
     */
    private String resourceGroupStartWith;

    /**
     * 集群资源组结束
     */
    private String resourceGroupEndWith;

    /**
     * 集群资源组包含
     */
    private String resourceGroupContains;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎种类列表
     */
    private Collection<String> engineCategoryIn;

    /**
     * 排除的引擎种类列表
     */
    private Collection<String> engineCategoryNotIn;

    /**
     * 引擎种类不为空
     */
    private Boolean engineCategoryIsNotEmpty;

    /**
     * 引擎种类为空
     */
    private Boolean engineCategoryIsEmpty;

    /**
     * 引擎种类开始
     */
    private String engineCategoryStartWith;

    /**
     * 引擎种类结束
     */
    private String engineCategoryEndWith;

    /**
     * 引擎种类包含
     */
    private String engineCategoryContains;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 引擎版本列表
     */
    private Collection<String> engineVersionIn;

    /**
     * 排除的引擎版本列表
     */
    private Collection<String> engineVersionNotIn;

    /**
     * 引擎版本不为空
     */
    private Boolean engineVersionIsNotEmpty;

    /**
     * 引擎版本为空
     */
    private Boolean engineVersionIsEmpty;

    /**
     * 引擎版本开始
     */
    private String engineVersionStartWith;

    /**
     * 引擎版本结束
     */
    private String engineVersionEndWith;

    /**
     * 引擎版本包含
     */
    private String engineVersionContains;

    /**
     * 初始优先级
     */
    private Integer priority;

    /**
     * 初始优先级列表
     */
    private Collection<Integer> priorityIn;

    /**
     * 排除的初始优先级列表
     */
    private Collection<Integer> priorityNotIn;

    /**
     * 最小初始优先级
     */
    private Integer priorityMin;

    /**
     * 最大初始优先级
     */
    private Integer priorityMax;

    /**
     * 运行优先级
     */
    private Integer runPri;

    /**
     * 运行优先级列表
     */
    private Collection<Integer> runPriIn;

    /**
     * 排除的运行优先级列表
     */
    private Collection<Integer> runPriNotIn;

    /**
     * 最小运行优先级
     */
    private Integer runPriMin;

    /**
     * 最大运行优先级
     */
    private Integer runPriMax;

    /**
     * 用户组
     */
    private String group;

    /**
     * 用户组列表
     */
    private Collection<String> groupIn;

    /**
     * 排除的用户组列表
     */
    private Collection<String> groupNotIn;

    /**
     * 用户组不为空
     */
    private Boolean groupIsNotEmpty;

    /**
     * 用户组为空
     */
    private Boolean groupIsEmpty;

    /**
     * 用户组开始
     */
    private String groupStartWith;

    /**
     * 用户组结束
     */
    private String groupEndWith;

    /**
     * 用户组包含
     */
    private String groupContains;

    /**
     * 用户
     */
    private String user;

    /**
     * 用户列表
     */
    private Collection<String> userIn;

    /**
     * 排除的用户列表
     */
    private Collection<String> userNotIn;

    /**
     * 用户不为空
     */
    private Boolean userIsNotEmpty;

    /**
     * 用户为空
     */
    private Boolean userIsEmpty;

    /**
     * 用户开始
     */
    private String userStartWith;

    /**
     * 用户结束
     */
    private String userEndWith;

    /**
     * 用户包含
     */
    private String userContains;

    /**
     * 集群应用ID
     */
    private String clusterAppId;

    /**
     * 集群应用ID列表
     */
    private Collection<String> clusterAppIdIn;

    /**
     * 排除的集群应用ID列表
     */
    private Collection<String> clusterAppIdNotIn;

    /**
     * 集群应用ID不为空
     */
    private Boolean clusterAppIdIsNotEmpty;

    /**
     * 集群应用ID为空
     */
    private Boolean clusterAppIdIsEmpty;

    /**
     * 集群应用ID开始
     */
    private String clusterAppIdStartWith;

    /**
     * 集群应用ID结束
     */
    private String clusterAppIdEndWith;

    /**
     * 集群应用ID包含
     */
    private String clusterAppIdContains;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态列表
     */
    private Collection<String> statusIn;

    /**
     * 排除的状态列表
     */
    private Collection<String> statusNotIn;

    /**
     * 状态不为空
     */
    private Boolean statusIsNotEmpty;

    /**
     * 状态为空
     */
    private Boolean statusIsEmpty;

    /**
     * 状态开始
     */
    private String statusStartWith;

    /**
     * 状态结束
     */
    private String statusEndWith;

    /**
     * 状态包含
     */
    private String statusContains;

    /**
     * 最小任务提交时间
     */
    private LocalDateTime startingTimeMin;

    /**
     * 最大任务提交时间
     */
    private LocalDateTime startingTimeMax;

    /**
     * 任务提交时间不为null
     */
    private Boolean startingTimeIsNotNull;

    /**
     * 任务提交时间为null
     */
    private Boolean startingTimeIsNull;

    /**
     * 最小任务执行开始时间
     */
    private LocalDateTime runningTimeMin;

    /**
     * 最大任务执行开始时间
     */
    private LocalDateTime runningTimeMax;

    /**
     * 任务执行开始时间不为null
     */
    private Boolean runningTimeIsNotNull;

    /**
     * 任务执行开始时间为null
     */
    private Boolean runningTimeIsNull;

    /**
     * 最小任务执行结束时间
     */
    private LocalDateTime finishTimeMin;

    /**
     * 最大任务执行结束时间
     */
    private LocalDateTime finishTimeMax;

    /**
     * 任务执行结束时间不为null
     */
    private Boolean finishTimeIsNotNull;

    /**
     * 任务执行结束时间为null
     */
    private Boolean finishTimeIsNull;

    /**
     * 最小创建时间
     */
    private LocalDateTime createTimeMin;

    /**
     * 最大创建时间
     */
    private LocalDateTime createTimeMax;

    /**
     * 最小更新时间
     */
    private LocalDateTime updateTimeMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime updateTimeMax;
}
