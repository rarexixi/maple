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
 * 集群查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterSelectCondition extends SelectCondition {

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
     * 集群类型
     */
    private String category;

    /**
     * 集群类型列表
     */
    private Collection<String> categoryIn;

    /**
     * 排除的集群类型列表
     */
    private Collection<String> categoryNotIn;

    /**
     * 集群类型不为空
     */
    private Boolean categoryIsNotEmpty;

    /**
     * 集群类型为空
     */
    private Boolean categoryIsEmpty;

    /**
     * 集群类型开始
     */
    private String categoryStartWith;

    /**
     * 集群类型结束
     */
    private String categoryEndWith;

    /**
     * 集群类型包含
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
     * 集群说明
     */
    private String desc;

    /**
     * 集群说明列表
     */
    private Collection<String> descIn;

    /**
     * 排除的集群说明列表
     */
    private Collection<String> descNotIn;

    /**
     * 集群说明不为空
     */
    private Boolean descIsNotEmpty;

    /**
     * 集群说明为空
     */
    private Boolean descIsEmpty;

    /**
     * 集群说明开始
     */
    private String descStartWith;

    /**
     * 集群说明结束
     */
    private String descEndWith;

    /**
     * 集群说明包含
     */
    private String descContains;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人列表
     */
    private Collection<Integer> createUserIn;

    /**
     * 排除的创建人列表
     */
    private Collection<Integer> createUserNotIn;

    /**
     * 最小创建人
     */
    private Integer createUserMin;

    /**
     * 最大创建人
     */
    private Integer createUserMax;

    /**
     * 修改人
     */
    private Integer updateUser;

    /**
     * 修改人列表
     */
    private Collection<Integer> updateUserIn;

    /**
     * 排除的修改人列表
     */
    private Collection<Integer> updateUserNotIn;

    /**
     * 最小修改人
     */
    private Integer updateUserMin;

    /**
     * 最大修改人
     */
    private Integer updateUserMax;

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
