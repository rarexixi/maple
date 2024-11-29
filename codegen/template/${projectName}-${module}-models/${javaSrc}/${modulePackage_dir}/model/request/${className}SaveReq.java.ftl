<#include "/include/table/properties.ftl">
package ${modulePackage}.model.request;

import ${commonPackage}.annotation.Jsr303ValidGroup;
import ${commonPackage}.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ${className}SaveReq extends BaseEntity {
    <#assign columnIndex = 0>
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">
    <#assign columnIndex = columnIndex+1>

    /**
     * ${columnFullComment}
     */
    <#if (column.pk)>
    <#if (column.autoIncrement)>
    <#--如果是自增主键，新增时可以为空，更新所有字段/更新部分字段 时都不可以为空-->
    @NotNull(message = "${fieldName}(${columnComment})不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    <#else>
    <#--如果不是自增主键，新增/更新所有字段/更新部分字段 时都不可以为空-->
    @${isString ? string('NotBlank','NotNull')}(message = "${fieldName}(${columnComment})不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    <#if (isInteger)>
    @Min(value = 1, message = "${fieldName}(${columnComment})必须大于0")
    </#if>
    </#if>
    <#elseif (!column.nullable && !(column.columnDefault??))>
    <#--如果不是主键，并且不可以为空，新增/更新所有字段 时都不可以为空-->
    @${isString ? string('NotBlank','NotNull')}(message = "${fieldName}(${columnComment})不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    </#if>
    private ${fieldType} ${fieldName};
    </#list>
}
