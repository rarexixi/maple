<#include "/include/table/properties.ftl">
package ${modulePackage}.model.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ${className}ListItemResponse implements Serializable {
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
