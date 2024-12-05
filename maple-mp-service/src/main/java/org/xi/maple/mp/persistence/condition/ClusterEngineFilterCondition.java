package org.xi.maple.mp.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 计算引擎查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineFilterCondition implements FilterCondition {

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
     * 所属集群
     */
    private String cluster;

    /**
     * 所属集群列表
     */
    private Collection<String> clusterIn;

    /**
     * 排除的所属集群列表
     */
    private Collection<String> clusterNotIn;

    /**
     * 所属集群不为空
     */
    private Boolean clusterIsNotEmpty;

    /**
     * 所属集群为空
     */
    private Boolean clusterIsEmpty;

    /**
     * 所属集群开始
     */
    private String clusterStartWith;

    /**
     * 所属集群结束
     */
    private String clusterEndWith;

    /**
     * 所属集群包含
     */
    private String clusterContains;

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 引擎名称列表
     */
    private Collection<String> nameIn;

    /**
     * 排除的引擎名称列表
     */
    private Collection<String> nameNotIn;

    /**
     * 引擎名称不为空
     */
    private Boolean nameIsNotEmpty;

    /**
     * 引擎名称为空
     */
    private Boolean nameIsEmpty;

    /**
     * 引擎名称开始
     */
    private String nameStartWith;

    /**
     * 引擎名称结束
     */
    private String nameEndWith;

    /**
     * 引擎名称包含
     */
    private String nameContains;

    /**
     * 引擎版本
     */
    private String version;

    /**
     * 引擎版本列表
     */
    private Collection<String> versionIn;

    /**
     * 排除的引擎版本列表
     */
    private Collection<String> versionNotIn;

    /**
     * 引擎版本不为空
     */
    private Boolean versionIsNotEmpty;

    /**
     * 引擎版本为空
     */
    private Boolean versionIsEmpty;

    /**
     * 引擎版本开始
     */
    private String versionStartWith;

    /**
     * 引擎版本结束
     */
    private String versionEndWith;

    /**
     * 引擎版本包含
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
