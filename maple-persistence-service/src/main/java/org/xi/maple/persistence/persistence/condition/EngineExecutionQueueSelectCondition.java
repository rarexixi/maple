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
 * 执行队列查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionQueueSelectCondition extends SelectCondition {

    /**
     * 执行队列名
     */
    private String queueName;

    /**
     * 执行队列名列表
     */
    private Collection<String> queueNameIn;

    /**
     * 排除的执行队列名列表
     */
    private Collection<String> queueNameNotIn;

    /**
     * 执行队列名不为空
     */
    private Boolean queueNameIsNotEmpty;

    /**
     * 执行队列名为空
     */
    private Boolean queueNameIsEmpty;

    /**
     * 执行队列名开始
     */
    private String queueNameStartWith;

    /**
     * 执行队列名结束
     */
    private String queueNameEndWith;

    /**
     * 执行队列名包含
     */
    private String queueNameContains;

    /**
     * 执行队列锁名
     */
    private String lockName;

    /**
     * 执行队列锁名列表
     */
    private Collection<String> lockNameIn;

    /**
     * 排除的执行队列锁名列表
     */
    private Collection<String> lockNameNotIn;

    /**
     * 执行队列锁名不为空
     */
    private Boolean lockNameIsNotEmpty;

    /**
     * 执行队列锁名为空
     */
    private Boolean lockNameIsEmpty;

    /**
     * 执行队列锁名开始
     */
    private String lockNameStartWith;

    /**
     * 执行队列锁名结束
     */
    private String lockNameEndWith;

    /**
     * 执行队列锁名包含
     */
    private String lockNameContains;

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
     * 队列优先级
     */
    private Integer priority;

    /**
     * 队列优先级列表
     */
    private Collection<Integer> priorityIn;

    /**
     * 排除的队列优先级列表
     */
    private Collection<Integer> priorityNotIn;

    /**
     * 最小队列优先级
     */
    private Integer priorityMin;

    /**
     * 最大队列优先级
     */
    private Integer priorityMax;

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
