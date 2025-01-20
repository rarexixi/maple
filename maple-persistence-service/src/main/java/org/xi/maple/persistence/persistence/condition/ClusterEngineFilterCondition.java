package org.xi.maple.persistence.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.model.db.condition.FilterCondition;

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
}
