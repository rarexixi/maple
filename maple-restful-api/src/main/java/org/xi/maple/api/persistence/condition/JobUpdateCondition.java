package org.xi.maple.api.persistence.condition;

import lombok.*;

import org.xi.maple.common.model.UpdateCondition;

/**
 * 执行作业更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Data
public class JobUpdateCondition extends UpdateCondition {

    /**
     * 作业ID
     */
    private Integer id;
}