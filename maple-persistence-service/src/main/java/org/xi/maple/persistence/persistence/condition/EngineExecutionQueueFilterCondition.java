package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行队列查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class EngineExecutionQueueFilterCondition implements FilterCondition {

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 来源应用列表
     */
    private Collection<String> fromAppIn;

    /**
     * 最小更新时间
     */
    private LocalDateTime updatedAtMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime updatedAtMax;
}
