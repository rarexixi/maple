package org.xi.maple.datasource.presentation.condition;

import org.xi.maple.common.models.SelectCondition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源配置查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceSelectCondition extends SelectCondition {

    /**
     * Id
     */
    private Integer id;

    /**
     * Id列表
     */
    private Collection<Integer> idIn;

    /**
     * 排除的Id列表
     */
    private Collection<Integer> idNotIn;

    /**
     * 最小Id
     */
    private Integer idMin;

    /**
     * 最大Id
     */
    private Integer idMax;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称列表
     */
    private Collection<String> nameIn;

    /**
     * 排除的名称列表
     */
    private Collection<String> nameNotIn;

    /**
     * 名称不为空
     */
    private Boolean nameIsNotEmpty;

    /**
     * 名称为空
     */
    private Boolean nameIsEmpty;

    /**
     * 名称开始
     */
    private String nameStartWith;

    /**
     * 名称结束
     */
    private String nameEndWith;

    /**
     * 名称包含
     */
    private String nameContains;

    /**
     * 描述
     */
    private String description;

    /**
     * 描述列表
     */
    private Collection<String> descIn;

    /**
     * 排除的描述列表
     */
    private Collection<String> descNotIn;

    /**
     * 描述不为空
     */
    private Boolean descIsNotEmpty;

    /**
     * 描述为空
     */
    private Boolean descIsEmpty;

    /**
     * 描述开始
     */
    private String descStartWith;

    /**
     * 描述结束
     */
    private String descEndWith;

    /**
     * 描述包含
     */
    private String descContains;

    /**
     * 类型
     */
    private String datasourceType;

    /**
     * 类型列表
     */
    private Collection<String> datasourceTypeIn;

    /**
     * 排除的类型列表
     */
    private Collection<String> datasourceTypeNotIn;

    /**
     * 类型不为空
     */
    private Boolean datasourceTypeIsNotEmpty;

    /**
     * 类型为空
     */
    private Boolean datasourceTypeIsEmpty;

    /**
     * 类型开始
     */
    private String datasourceTypeStartWith;

    /**
     * 类型结束
     */
    private String datasourceTypeEndWith;

    /**
     * 类型包含
     */
    private String datasourceTypeContains;

    /**
     * 数据源版本
     */
    private String version;

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
