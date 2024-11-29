<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.condition;

import ${commonPackage}.model.db.condition.FilterCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ${table.comment}更新条件
 *
 * @author ${author}
 */
@Getter
@Setter
@ToString
public class ${className}PkCondition implements FilterCondition {
    <#list pks as column>
    <#include "/include/column/properties.ftl">

    /**
     * ${columnFullComment}
     */
    private ${fieldType} ${fieldName};
    <#if (table.hasUniPk)>

    /**
     * ${columnFullComment}集合
     */
    private Collection<${fieldType}> ${fieldName}In;
    </#if>
    </#list>
}
