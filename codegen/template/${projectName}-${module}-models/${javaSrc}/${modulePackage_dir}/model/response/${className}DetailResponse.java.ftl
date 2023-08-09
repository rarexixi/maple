<#include "/include/table/properties.ftl">
package ${modulePackage}.model.response;

import java.math.BigDecimal;
import java.util.Collection;

import lombok.Data;

@Data
public class ${className}DetailResponse extends ${className}ListItemResponse {
    <#list table.fkSelectColumns as column>
    <#include "/include/column/properties.ftl">

    /**
     * ${columnFullComment}
     */
    private String ${fieldNameExceptKey}Text;
    </#list>
}
