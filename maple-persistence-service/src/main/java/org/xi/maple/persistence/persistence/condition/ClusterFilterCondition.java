package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

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
public class ClusterFilterCondition implements FilterCondition {

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群类型
     */
    private String category;
}
