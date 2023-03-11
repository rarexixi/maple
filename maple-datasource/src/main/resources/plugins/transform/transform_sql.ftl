<#include "../includes/variables.ftl">
<#if (config.sql?matches(".*\\$\\{.*?}.*", "s"))>var<#else>val</#if> ${prefix}Sql = <@str content=config.sql/>
<#if (config.sql?matches(".*\\$\\{.*?}.*", "s"))>
${prefix}Sql = VariableUtils.replaceVariables(sql, ${prefix}Variables.asJava)
</#if>
val ${prefix}DF = spark.sql(${prefix}Sql)
${prefix}DF.createOrReplaceTempView("${config.resultTable}")
<#if (config.persist)>
${prefix}DF.persist(StorageLevel.${config.storageLevel})
</#if>

