package org.xi.maple.mp.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统配置更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class SysConfPkCondition implements FilterCondition {

    /**
     * 配置键
     */
    private String confKey;

    /**
     * 配置键集合
     */
    private Collection<String> confKeyIn;
}
