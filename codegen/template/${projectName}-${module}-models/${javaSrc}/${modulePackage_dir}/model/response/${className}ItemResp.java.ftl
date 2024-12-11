<#include "/include/table/properties.ftl">
package ${modulePackage}.model.response;

import lombok.Data;

import java.io.Serializable;
<#include "/include/table/table_field_type_imports.ftl">
<@import_fields_type table.columns />

@Data
public class ${className}ItemResp implements Serializable {
    <#assign columnIndex = 0>
    <#list table.columns as column>
    <#include "/include/column/properties.ftl">

    /**
     * ${columnFullComment}
     */
    <#assign columnIndex = columnIndex+1>
    private ${fieldType} ${fieldName};
    </#list>
}
