<#include "/include/table/properties.ftl">
package ${modulePackage}.model.request;

import ${modulePackage}.model.BaseEntity;
import java.util.Collection;

import lombok.Data;

@Data
public class ${className}PatchRequest extends BaseEntity {
    <#list pks as column>
    <#include "/include/column/properties.ftl">

    private ${fieldType} ${fieldName};
    </#list>
    <#if (table.hasUniPk)>

    private Collection<${fieldType}> ${fieldName}s;
    </#if>
}
