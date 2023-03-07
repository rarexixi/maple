<#if (config.variables?size > 0)>
<#assign keys = config.variables?keys>
val ${prefix}Variables = Map(
  <#list keys as key>
  "${key}" -> "${config.variables[key]}"<#sep>,</#sep>
  </#list>
) ++ globalVariables
<#else>
val ${prefix}Variables = globalVariables
</#if>