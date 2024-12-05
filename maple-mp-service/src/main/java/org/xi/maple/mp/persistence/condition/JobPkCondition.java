package org.xi.maple.mp.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行作业更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class JobPkCondition implements FilterCondition {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业ID集合
     */
    private Collection<Integer> idIn;
}
