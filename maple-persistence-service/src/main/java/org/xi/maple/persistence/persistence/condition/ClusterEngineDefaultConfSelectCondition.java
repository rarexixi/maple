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
 * 集群引擎默认配置查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineDefaultConfSelectCondition extends SelectCondition {

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
     * 主体类型
     */
    private String objType;

    /**
     * 主体类型列表
     */
    private Collection<String> objTypeIn;

    /**
     * 排除的主体类型列表
     */
    private Collection<String> objTypeNotIn;

    /**
     * 主体类型不为空
     */
    private Boolean objTypeIsNotEmpty;

    /**
     * 主体类型为空
     */
    private Boolean objTypeIsEmpty;

    /**
     * 主体类型开始
     */
    private String objTypeStartWith;

    /**
     * 主体类型结束
     */
    private String objTypeEndWith;

    /**
     * 主体类型包含
     */
    private String objTypeContains;

    /**
     * 所属主体
     */
    private String objName;

    /**
     * 所属主体列表
     */
    private Collection<String> objNameIn;

    /**
     * 排除的所属主体列表
     */
    private Collection<String> objNameNotIn;

    /**
     * 所属主体不为空
     */
    private Boolean objNameIsNotEmpty;

    /**
     * 所属主体为空
     */
    private Boolean objNameIsEmpty;

    /**
     * 所属主体开始
     */
    private String objNameStartWith;

    /**
     * 所属主体结束
     */
    private String objNameEndWith;

    /**
     * 所属主体包含
     */
    private String objNameContains;

    /**
     * 集群引擎ID
     */
    private Integer engineId;

    /**
     * 集群引擎ID列表
     */
    private Collection<Integer> engineIdIn;

    /**
     * 排除的集群引擎ID列表
     */
    private Collection<Integer> engineIdNotIn;

    /**
     * 最小集群引擎ID
     */
    private Integer engineIdMin;

    /**
     * 最大集群引擎ID
     */
    private Integer engineIdMax;
}
