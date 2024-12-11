<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${commonPackage}.model.BaseEntity;

<#include "/include/table/table_field_type_imports.ftl">
<@import_fields_type table.columnsExceptBase />

/**
 * ${table.comment}实体
 *
 * @author ${author}
 */
@Getter
@Setter
@ToString
public class ${className}Entity extends BaseEntity {
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">

    /**
     * ${columnFullComment}
     */
    private ${fieldType} ${fieldName};
    </#list>
}
