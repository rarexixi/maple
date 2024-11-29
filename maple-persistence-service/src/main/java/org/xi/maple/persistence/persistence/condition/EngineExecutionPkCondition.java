package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 引擎执行记录更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionPkCondition implements FilterCondition {

    /**
     * 执行ID
     */
    private Integer id;

    /**
     * 执行ID集合
     */
    private Collection<Integer> idIn;
}
