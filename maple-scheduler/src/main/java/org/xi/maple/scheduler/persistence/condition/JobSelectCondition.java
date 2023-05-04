package org.xi.maple.scheduler.persistence.condition;

import lombok.Data;
import org.xi.maple.common.model.SelectCondition;

/**
 * 执行作业更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Data
public class JobSelectCondition extends SelectCondition {

    /**
     * 作业ID
     */
    private Integer id;
}