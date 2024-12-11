<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.condition;

import ${commonPackage}.model.db.condition.FilterCondition;

<#include "/include/table/table_field_type_imports.ftl">
<@import_fields_type pks />
<#if (table.hasUniPk)>
import java.util.Collection;
</#if>

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
