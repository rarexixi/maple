package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.SelectCondition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
     * 执行标识
     */
    private String uniqueId;

    /**
     * 执行标识列表
     */
    private Collection<String> uniqueIdIn;

    /**
     * 排除的执行标识列表
     */
    private Collection<String> uniqueIdNotIn;

    /**
     * 执行标识不为空
     */
    private Boolean uniqueIdIsNotEmpty;

    /**
     * 执行标识为空
     */
    private Boolean uniqueIdIsEmpty;

    /**
     * 执行标识开始
     */
    private String uniqueIdStartWith;

    /**
     * 执行标识结束
     */
    private String uniqueIdEndWith;

    /**
     * 执行标识包含
     */
    private String uniqueIdContains;

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
     * 作业说明
     */
    private String execComment;

    /**
     * 作业说明列表
     */
    private Collection<String> execCommentIn;

    /**
     * 排除的作业说明列表
     */
    private Collection<String> execCommentNotIn;

    /**
     * 作业说明不为空
     */
    private Boolean execCommentIsNotEmpty;

    /**
     * 作业说明为空
     */
    private Boolean execCommentIsEmpty;

    /**
     * 作业说明开始
     */
    private String execCommentStartWith;

    /**
     * 作业说明结束
     */
    private String execCommentEndWith;

    /**
     * 作业说明包含
     */
    private String execCommentContains;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行内容类型列表
     */
    private Collection<String> contentTypeIn;

    /**
     * 排除的执行内容类型列表
     */
    private Collection<String> contentTypeNotIn;

    /**
     * 执行内容类型不为空
     */
    private Boolean contentTypeIsNotEmpty;

    /**
     * 执行内容类型为空
     */
    private Boolean contentTypeIsEmpty;

    /**
     * 执行内容类型开始
     */
    private String contentTypeStartWith;

    /**
     * 执行内容类型结束
     */
    private String contentTypeEndWith;

    /**
     * 执行内容类型包含
     */
    private String contentTypeContains;

    /**
     * 执行内容路径
     */
    private String contentPath;

    /**
     * 执行内容路径列表
     */
    private Collection<String> contentPathIn;

    /**
     * 排除的执行内容路径列表
     */
    private Collection<String> contentPathNotIn;

    /**
     * 执行内容路径不为空
     */
    private Boolean contentPathIsNotEmpty;

    /**
     * 执行内容路径为空
     */
    private Boolean contentPathIsEmpty;

    /**
     * 执行内容路径开始
     */
    private String contentPathStartWith;

    /**
     * 执行内容路径结束
     */
    private String contentPathEndWith;

    /**
     * 执行内容路径包含
     */
    private String contentPathContains;

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
     * 集群队列
     */
    private String clusterQueue;

    /**
     * 集群队列列表
     */
    private Collection<String> clusterQueueIn;

    /**
     * 排除的集群队列列表
     */
    private Collection<String> clusterQueueNotIn;

    /**
     * 集群队列不为空
     */
    private Boolean clusterQueueIsNotEmpty;

    /**
     * 集群队列为空
     */
    private Boolean clusterQueueIsEmpty;

    /**
     * 集群队列开始
     */
    private String clusterQueueStartWith;

    /**
     * 集群队列结束
     */
    private String clusterQueueEndWith;

    /**
     * 集群队列包含
     */
    private String clusterQueueContains;

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
    private Integer runPriority;

    /**
     * 运行优先级列表
     */
    private Collection<Integer> runPriorityIn;

    /**
     * 排除的运行优先级列表
     */
    private Collection<Integer> runPriorityNotIn;

    /**
     * 最小运行优先级
     */
    private Integer runPriorityMin;

    /**
     * 最大运行优先级
     */
    private Integer runPriorityMax;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, SUCCEED, FAILED, KILLED)
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
     * 回调地址
     */
    private String webhooks;

    /**
     * 回调地址列表
     */
    private Collection<String> webhooksIn;

    /**
     * 排除的回调地址列表
     */
    private Collection<String> webhooksNotIn;

    /**
     * 回调地址不为空
     */
    private Boolean webhooksIsNotEmpty;

    /**
     * 回调地址为空
     */
    private Boolean webhooksIsEmpty;

    /**
     * 回调地址开始
     */
    private String webhooksStartWith;

    /**
     * 回调地址结束
     */
    private String webhooksEndWith;

    /**
     * 回调地址包含
     */
    private String webhooksContains;

    /**
     * 最小创建时间
     */
    private LocalDateTime startTimeMin;

    /**
     * 最大创建时间
     */
    private LocalDateTime startTimeMax;

    /**
     * 创建时间不为null
     */
    private Boolean startTimeIsNotNull;

    /**
     * 创建时间为null
     */
    private Boolean startTimeIsNull;

    /**
     * 最小停止时间
     */
    private LocalDateTime endTimeMin;

    /**
     * 最大停止时间
     */
    private LocalDateTime endTimeMax;

    /**
     * 停止时间不为null
     */
    private Boolean endTimeIsNotNull;

    /**
     * 停止时间为null
     */
    private Boolean endTimeIsNull;

    /**
     * 最小更新时间
     */
    private LocalDateTime heartbeatTimeMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime heartbeatTimeMax;

    /**
     * 更新时间不为null
     */
    private Boolean heartbeatTimeIsNotNull;

    /**
     * 更新时间为null
     */
    private Boolean heartbeatTimeIsNull;

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
