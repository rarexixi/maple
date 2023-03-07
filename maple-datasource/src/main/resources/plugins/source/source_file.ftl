<#include "../includes/variables.ftl">
<#assign hasColumnNames = (config.columnNames??) && (config.columnNames?size > 0)>
<#if (config.path?starts_with("/"))>
<#assign path = "hdfs://" + config.path>
<#else>
  <#assign path = config.path>
</#if>
val ${prefix}Path = <#if (config.path?matches(".*\\$\\{.*?}.*", "s"))>VariableUtils.replaceVariables("${path}", ${prefix}Variables.asJava)<#else>"${path}"</#if>
<#if hasColumnNames>var<#else>val</#if> ${prefix}DF = spark.read
<#assign keys = config.options?keys>
<#list keys as key>
  .option("${key}", "${config.options[key]}")
</#list>
  .format("${config.serializer}")
  .load(VariableUtils.replaceVariables(${prefix}Path, ${prefix}Variables.asJava))

<#if hasColumnNames>
val ${prefix}ColumnNames = Array(<#list config.columnNames as columnName>"${columnName}"<#sep>, </#list>)
${prefix}DF = ${prefix}DF.toDF(${prefix}ColumnNames: _*)
</#if>
${prefix}DF.createOrReplaceTempView("${config.resultTable}")
<#if (config.persist)>
${prefix}DF.persist(StorageLevel.${config.storageLevel})
</#if>

