<#include "../includes/variables.ftl">
<#if (config.path?starts_with("/"))>
<#assign path = "hdfs://" + config.path>
<#else>
  <#assign path = config.path>
</#if>
val ${prefix}Path = <#if (config.path?matches(".*\\$\\{.*?}.*", "s"))>VariableUtils.replaceVariables("${path}", ${prefix}Variables.asJava)<#else>"${path}"</#if>
<#include "./includes/source_df.ftl">
  .write.format("${config.serializer}")
<#assign keys = config.options?keys>
<#list keys as key>
  .option("${key}", "${config.options[key]}")
</#list>
  .mode("${config.saveMode}")
  .save(${prefix}Path)

