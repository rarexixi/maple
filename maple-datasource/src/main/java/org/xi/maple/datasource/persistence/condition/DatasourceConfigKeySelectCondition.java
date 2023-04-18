package org.xi.maple.datasource.persistence.condition;

import org.xi.maple.common.model.SelectCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源配置项查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceConfigKeySelectCondition extends SelectCondition {

    /**
     * 类型
     */
    private String datasourceType;

    /**
     * 类型编码包含
     */
    private Collection<String> datasourceTypeIn;

    /**
     * 是否删除
     */
    private Integer deleted;
}
