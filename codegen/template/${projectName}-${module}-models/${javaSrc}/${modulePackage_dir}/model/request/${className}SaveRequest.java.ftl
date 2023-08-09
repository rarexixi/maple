<#include "/include/table/properties.ftl">
package ${modulePackage}.model.request;

import ${modulePackage}.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ${className}SaveRequest extends BaseEntity {
    <#assign columnIndex = 0>
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">
    <#assign columnIndex = columnIndex+1>

    /**
     * ${columnFullComment}
     */
    <#if (column.pk && column.autoIncrement)>
    @NotNull(message = "${fieldName}(${columnComment})不能为空")
    </#if>
    private ${fieldType} ${fieldName};
    </#list>
}
