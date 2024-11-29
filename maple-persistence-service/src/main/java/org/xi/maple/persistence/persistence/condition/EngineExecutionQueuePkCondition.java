package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行队列更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionQueuePkCondition implements FilterCondition {

    /**
     * 执行队列名
     */
    private String queueName;

    /**
     * 执行队列名集合
     */
    private Collection<String> queueNameIn;
}
