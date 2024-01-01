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
 * 集群引擎查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineSelectCondition extends SelectCondition {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 引擎ID列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的引擎ID列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小引擎ID
     */
    private Integer idMin;

    /**
     * 最大引擎ID
     */
    private Integer idMax;

    /**
     * 集群名称
     */
    private String cluster;

    /**
     * 集群名称列表
     */
    private Collection<String> clusterIn;

    /**
     * 排除的集群名称列表
     */
    private Collection<String> clusterNotIn;

    /**
     * 集群名称不为空
     */
    private Boolean clusterIsNotEmpty;

    /**
     * 集群名称为空
     */
    private Boolean clusterIsEmpty;

    /**
     * 集群名称开始
     */
    private String clusterStartWith;

    /**
     * 集群名称结束
     */
    private String clusterEndWith;

    /**
     * 集群名称包含
     */
    private String clusterContains;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型名称列表
     */
    private Collection<String> nameIn;

    /**
     * 排除的类型名称列表
     */
    private Collection<String> nameNotIn;

    /**
     * 类型名称不为空
     */
    private Boolean nameIsNotEmpty;

    /**
     * 类型名称为空
     */
    private Boolean nameIsEmpty;

    /**
     * 类型名称开始
     */
    private String nameStartWith;

    /**
     * 类型名称结束
     */
    private String nameEndWith;

    /**
     * 类型名称包含
     */
    private String nameContains;

    /**
     * 类型版本
     */
    private String version;

    /**
     * 类型版本列表
     */
    private Collection<String> versionIn;

    /**
     * 排除的类型版本列表
     */
    private Collection<String> versionNotIn;

    /**
     * 类型版本不为空
     */
    private Boolean versionIsNotEmpty;

    /**
     * 类型版本为空
     */
    private Boolean versionIsEmpty;

    /**
     * 类型版本开始
     */
    private String versionStartWith;

    /**
     * 类型版本结束
     */
    private String versionEndWith;

    /**
     * 类型版本包含
     */
    private String versionContains;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 引擎目录列表
     */
    private Collection<String> engineHomeIn;

    /**
     * 排除的引擎目录列表
     */
    private Collection<String> engineHomeNotIn;

    /**
     * 引擎目录不为空
     */
    private Boolean engineHomeIsNotEmpty;

    /**
     * 引擎目录为空
     */
    private Boolean engineHomeIsEmpty;

    /**
     * 引擎目录开始
     */
    private String engineHomeStartWith;

    /**
     * 引擎目录结束
     */
    private String engineHomeEndWith;

    /**
     * 引擎目录包含
     */
    private String engineHomeContains;

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
