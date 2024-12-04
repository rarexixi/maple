package org.xi.maple.mp.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 集群更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterPkCondition implements FilterCondition {

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群名称集合
     */
    private Collection<String> nameIn;
}
