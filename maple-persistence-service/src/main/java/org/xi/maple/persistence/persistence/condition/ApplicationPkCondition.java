package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

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
public class ApplicationPkCondition implements FilterCondition {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用名称集合
     */
    private Collection<String> appNameIn;
}
