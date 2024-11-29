<#assign tableFullComment = table.comment>
<#assign tableComment = (tableFullComment?split("[（ ,，(]", "r"))[0]>
<#assign tableName = table.tableName>
<#assign targetTableName = table.targetTableName>
<#assign tablePluralPath = table.pluralTableName?replace("_", "-")>
<#assign tablePath = targetTableName?replace("_", "-")>
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLower = className?lower_case>
<#assign pks = table.pks>
<#assign classNameLength = className?length>
<#assign hasValidStatusColumn = table.validStatusColumn??>
<#if hasValidStatusColumn>
    <#assign validStatusColumn = table.validStatusColumn>
    <#assign validStatusPropertyName = validStatusColumn.targetName>
    <#assign validStatusFieldName = validStatusPropertyName?uncap_first>
</#if>
<#if table.uniPk??>
    <#assign uniPk = table.uniPk>
    <#assign uniPkFullComment = uniPk.columnComment>
    <#assign uniPkComment = (uniPkFullComment?split("[（ ,，(：:]", "r"))[0]>
    <#assign uniPkPropertyName = uniPk.targetName>
    <#assign uniPkFieldName = uniPkPropertyName?uncap_first>
    <#assign uniPkFieldType = uniPk.targetDataType>

    <#assign uniPkCanBeEqual = (uniPk.validStatus || uniPk.dataType?contains("int") || uniPk.dataType == "date" || uniPk.dataType?ends_with("char"))>
    <#assign uniPkCanBeList = (!uniPk.validStatus && (uniPk.dataType?contains("int") || uniPk.dataType?contains("date") || uniPk.dataType?ends_with("char")))>
    <#assign uniPkCanBeRange = (!uniPk.validStatus && (uniPk.dataType?contains("int") || uniPk.dataType == "double" || uniPk.dataType == "float" || uniPk.dataType == "decimal" || uniPk.dataType == "numeric" || uniPk.dataType?contains("date") || uniPk.dataType?contains("time")))>
    <#assign uniPkCanBeNull = uniPk.nullable>

    <#assign uniPkIsInteger = (uniPk.dataType?contains("int"))>
    <#assign uniPkIsString = (uniPk.dataType?ends_with("char"))>
    <#assign uniPkFieldBasicType = uniPkFieldType>
    <#if (uniPkFieldType=="Character")>
        <#assign uniPkFieldBasicType = "char">
    <#elseif (uniPkFieldType=="Short")>
        <#assign uniPkFieldBasicType = "short">
    <#elseif (uniPkFieldType=="Integer")>
        <#assign uniPkFieldBasicType = "int">
    <#elseif (uniPkFieldType=="Long")>
        <#assign uniPkFieldBasicType = "long">
    </#if>
    <#assign hasUniId = (table.hasAutoIncUniPk && (uniPkFieldType == "Integer") && (uniPkFieldName == "id"))>
</#if>
<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro $ value>${r"$"}{${value}}</#macro>
<#macro @>@</#macro>
<#macro pkTrav type hasMoreParams=false leftOffset="">
<#--主键的方法名，多个字段之间用 "And" 分隔-->
<#if (type == "pk_fun_names")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">${propertyName}<#if (column?has_next)>And</#if></#list>
<#--主键的参数值，无论是单主键还是多主键，字段名上都不添加List-->
</#compress><#elseif (type == "pk_values")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">${fieldName}<#if (hasMoreParams || column?has_next)>, </#if></#list>
<#--主键的参数值，如果是单主键，则字段名上添加List-->
</#compress><#elseif (type == "batch_pk_values")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">${fieldName}<#if (table.hasUniPk)>List</#if><#if (hasMoreParams || column?has_next)>, </#if></#list>
<#--主键的请求路径，无论是单主键还是多主键，字段名上都不添加List-->
</#compress><#elseif (type == "pk_path")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}}</#list>
<#--主键的请求路径，如果是单主键，则字段名上添加List-->
</#compress><#elseif (type == "batch_pk_path")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}<#if (table.hasUniPk)>List</#if>}</#list>
<#--主键的方法参数，无论是单主键还是多主键，字段名上都不添加List-->
</#compress><#elseif (type == "pk_params")><#compress>
<#list pks as column><#include "/include/column/properties.ftl">${fieldType} ${fieldName}<#if (hasMoreParams || column?has_next)>, </#if></#list>
<#--主键的方法参数，如果是单主键，则字段名上添加List-->
</#compress><#elseif (type == "batch_pk_params")><#compress>
<#list pks as column><#include "/include/column/properties.ftl"><#if (table.hasUniPk)>List<${fieldType}> ${fieldName}List<#if (hasMoreParams || column?has_next)>, </#if><#else>${fieldType} ${fieldName}<#if (hasMoreParams || column?has_next)>, </#if></#if></#list>
<#--主键的请求参数，无论是单主键还是多主键，字段名上都不添加List-->
</#compress><#elseif (type == "pk_req_params")>
<#list pks as column>
<#include "/include/column/properties.ftl">
${leftOffset}@PathVariable("${fieldName}") ${isString ? string('@NotBlank','@NotNull')}(message = "${fieldName}(${columnComment})不能为空")<#if isInteger> @Min(value = 1, message = "${fieldName}(${columnComment})必须大于0")</#if> ${fieldType} ${fieldName}<#if (hasMoreParams || column?has_next)>,</#if>
</#list>
<#--主键的请求参数，如果是单主键，则字段名上添加List-->
<#elseif (type == "batch_pk_req_params")>
<#list pks as column>
<#include "/include/column/properties.ftl">
<#if (table.hasUniPk)>
${leftOffset}@PathVariable("${fieldName}List") @Validated List<${isString ? string('@NotBlank','@NotNull')}(message = "${fieldName}(${columnComment})不能为空")<#if isInteger> @Min(value = 1, message = "${fieldName}(${columnComment})必须大于0")</#if> ${fieldType}> ${fieldName}List<#if (hasMoreParams || column?has_next)>,</#if>
<#else>
${leftOffset}@PathVariable("${fieldName}") ${isString ? string('@NotBlank','@NotNull')}(message = "${fieldName}(${columnComment})不能为空")<#if isInteger> @Min(value = 1, message = "${fieldName}(${columnComment})必须大于0")</#if> ${fieldType} ${fieldName}<#if (hasMoreParams || column?has_next)>,</#if>
</#if>
</#list>
<#--主键的参数方法注释，无论是单主键还是多主键，字段名上都不添加List-->
<#elseif (type == "pk_params_comment")>
<#list pks as column>
<#include "/include/column/properties.ftl">
${leftOffset}* @param ${fieldName} ${columnComment}
</#list>
<#--主键的参数方法注释，如果是单主键，则字段名上添加List-->
<#elseif (type == "batch_pk_params_comment")>
<#list pks as column>
<#include "/include/column/properties.ftl">
<#if (table.hasUniPk)>
${leftOffset}* @param ${fieldName}List ${columnComment}列表
<#else>
${leftOffset}* @param ${fieldName} ${columnComment}
</#if>
</#list>
</#if>
</#macro>