package org.xi.maple.datasource.presentation.condition;

import org.xi.maple.common.models.UpdateCondition;

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
public class DatasourceTypeUpdateCondition extends UpdateCondition {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型编码列表
     */
    private Collection<String> typeCodes;
}
