<#macro import_fields_type columns needCollection=false>
<#assign hasBigDecimal = false>
<#assign hasLocalDate = false>
<#assign hasLocalDateTime = false>
<#assign hasLocalTime = false>
<#assign hasCollection = false>
<#list columns as column>
<#include "/include/column/properties.ftl">
<#if (fieldType == "BigDecimal")>
    <#assign hasBigDecimal = true>
<#elseif (fieldType == "LocalDate")>
    <#assign hasLocalDate = true>
<#elseif (fieldType == "LocalDateTime")>
    <#assign hasLocalDateTime = true>
<#elseif (fieldType == "LocalTime")>
    <#assign hasLocalTime = true>
<#elseif (canBeList)>
    <#assign hasCollection = true>
</#if>
</#list>
<#if (fieldType == "BigDecimal")>
import java.math.BigDecimal;
</#if>
<#if (fieldType == "LocalDate")>
import java.time.LocalDate;
</#if>
<#if (fieldType == "LocalDateTime")>
import java.time.LocalDateTime;
</#if>
<#if (fieldType == "LocalTime")>
import java.time.LocalTime;
</#if>
<#if (hasCollection && needCollection)>
import java.util.Collection;
</#if>
</#macro>