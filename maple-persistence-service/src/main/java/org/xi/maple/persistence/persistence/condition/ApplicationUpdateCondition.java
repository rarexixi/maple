package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.UpdateCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 访问程序更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ApplicationUpdateCondition extends UpdateCondition {

    /**
     * 应用名称
     */
    private String appName;
}
