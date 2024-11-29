package org.xi.maple.mp.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源类型更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceTypePkCondition implements FilterCondition {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型编码集合
     */
    private Collection<String> typeCodeIn;
}
