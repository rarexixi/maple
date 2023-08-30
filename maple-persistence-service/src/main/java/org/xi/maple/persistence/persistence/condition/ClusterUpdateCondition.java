package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.UpdateCondition;

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
public class ClusterUpdateCondition extends UpdateCondition {

    /**
     * 集群名称
     */
    private String name;
}
