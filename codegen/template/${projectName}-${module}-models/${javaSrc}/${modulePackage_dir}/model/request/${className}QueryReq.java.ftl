<#include "/include/table/properties.ftl">
package ${modulePackage}.model.request;

import lombok.Data;
import ${commonPackage}.constant.SortConstants;
import ${commonPackage}.model.QueryReq;

import java.io.Serializable;
<#include "/include/table/table_field_type_imports.ftl">
<@import_fields_type table.indexes true />

@Data
public class ${className}QueryReq extends QueryReq {
    <#list table.indexes as column>
    <#include "/include/column/properties.ftl">
    <#if (canBeEqual)>

    private ${fieldType} ${fieldName};
    </#if>
    <#if (canBeList)>

    private Collection<${fieldType}> ${fieldName}In;
    </#if>
    <#if (canBeRange)>

    private ${fieldType} ${fieldName}Min;

    private ${fieldType} ${fieldName}Max;
    </#if>
    <#if (isString)>

    private ${fieldType} ${fieldName}Contains;
    </#if>
    </#list>
    <#if (hasValidStatusColumn)>

    private ${table.validStatusColumn.targetDataType} ${validStatusFieldName};
    </#if>
    <#list table.indexes as column>
    <#include "/include/column/properties.ftl">
    <#if (canBeRange)>

    public void set${propertyName}Range(${fieldType}[] ${fieldName}Range)  {
        if (${fieldName}Range == null || ${fieldName}Range.length != 2) {
            return;
        }
        this.${fieldName}Min = ${fieldName}Range[0];
        this.${fieldName}Max = ${fieldName}Range[1];
    }
    </#if>

    public void set${propertyName}Sort(SortConstants sortConstants)  {
        super.orderBy("${column.columnName}", sortConstants);
    }

    public void get${propertyName}Sort()  {
        super.getOrderBy().getOrDefault("${column.columnName}", null);
    }
    </#list>
}
