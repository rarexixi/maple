package org.xi.maple.mp.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 集群查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterFilterCondition implements FilterCondition {

    /**
     * 集群ID
     */
    private Integer id;

    /**
     * 集群ID列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的集群ID列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小集群ID
     */
    private Integer idMin;

    /**
     * 最大集群ID
     */
    private Integer idMax;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群名称列表
     */
    private Collection<String> nameIn;

    /**
     * 排除的集群名称列表
     */
    private Collection<String> nameNotIn;

    /**
     * 集群名称不为空
     */
    private Boolean nameIsNotEmpty;

    /**
     * 集群名称为空
     */
    private Boolean nameIsEmpty;

    /**
     * 集群名称开始
     */
    private String nameStartWith;

    /**
     * 集群名称结束
     */
    private String nameEndWith;

    /**
     * 集群名称包含
     */
    private String nameContains;

    /**
     * 集群种类
     */
    private String category;

    /**
     * 集群种类列表
     */
    private Collection<String> categoryIn;

    /**
     * 排除的集群种类列表
     */
    private Collection<String> categoryNotIn;

    /**
     * 集群种类不为空
     */
    private Boolean categoryIsNotEmpty;

    /**
     * 集群种类为空
     */
    private Boolean categoryIsEmpty;

    /**
     * 集群种类开始
     */
    private String categoryStartWith;

    /**
     * 集群种类结束
     */
    private String categoryEndWith;

    /**
     * 集群种类包含
     */
    private String categoryContains;

    /**
     * 集群地址
     */
    private String address;

    /**
     * 集群地址列表
     */
    private Collection<String> addressIn;

    /**
     * 排除的集群地址列表
     */
    private Collection<String> addressNotIn;

    /**
     * 集群地址不为空
     */
    private Boolean addressIsNotEmpty;

    /**
     * 集群地址为空
     */
    private Boolean addressIsEmpty;

    /**
     * 集群地址开始
     */
    private String addressStartWith;

    /**
     * 集群地址结束
     */
    private String addressEndWith;

    /**
     * 集群地址包含
     */
    private String addressContains;

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
