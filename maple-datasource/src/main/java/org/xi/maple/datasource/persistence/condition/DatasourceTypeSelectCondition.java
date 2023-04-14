package org.xi.maple.datasource.persistence.condition;

import org.xi.maple.common.models.SelectCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源类型查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceTypeSelectCondition extends SelectCondition {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型编码包含
     */
    private Collection<String> typeCodeIn;

    /**
     * 类型编码包含
     */
    private String typeCodeContains;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 类型名称包含
     */
    private String typeNameContains;

    /**
     * 分类
     */
    private String classifier;

    /**
     * 是否删除
     */
    private Integer deleted;
}
