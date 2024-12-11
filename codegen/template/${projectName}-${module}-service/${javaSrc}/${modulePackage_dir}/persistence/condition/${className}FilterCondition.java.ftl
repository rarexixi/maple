<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${commonPackage}.model.db.condition.FilterCondition;

<#include "/include/table/table_field_type_imports.ftl">
<@import_fields_type table.columns true />

/**
 * ${table.comment}查询条件
 *
 * @author ${author}
 */
@Getter
@Setter
@ToString
public class ${className}FilterCondition implements FilterCondition {
<#list table.columns as column>
<#include "/include/column/properties.ftl">
<#if (column.ignoreSearch || isContent)>
<#else>
    <#if (canBeEqual)>

    /**
     * ${columnFullComment}
     */
    private ${fieldType} ${fieldName};
    </#if>
    <#if (canBeList)>

    /**
     * ${columnComment}列表
     */
    private Collection<${fieldType}> ${fieldName}In;

    /**
     * 排除的${columnComment}列表
     */
    private Collection<${fieldType}> ${fieldName}NotIn;
    </#if>
    <#if (canBeRange)>

    /**
     * 最小${columnComment}
     */
    private ${fieldType} ${fieldName}Min;

    /**
     * 最大${columnComment}
     */
    private ${fieldType} ${fieldName}Max;
    </#if>
    <#if (canBeNull)>

    /**
     * ${columnComment}不为null
     */
    private Boolean ${fieldName}IsNotNull;

    /**
     * ${columnComment}为null
     */
    private Boolean ${fieldName}IsNull;
    </#if>
    <#if (isString)>

    /**
     * ${columnComment}不为空
     */
    private Boolean ${fieldName}IsNotEmpty;

    /**
     * ${columnComment}为空
     */
    private Boolean ${fieldName}IsEmpty;

    /**
     * ${columnComment}开始
     */
    private ${fieldType} ${fieldName}StartWith;

    /**
     * ${columnComment}结束
     */
    private ${fieldType} ${fieldName}EndWith;

    /**
     * ${columnComment}包含
     */
    private ${fieldType} ${fieldName}Contains;
    </#if>
</#if>
</#list>
}
