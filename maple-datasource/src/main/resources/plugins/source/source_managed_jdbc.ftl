<#include "../includes/variables.ftl">
<#if (config.query?matches(".*\\$\\{.*?}.*", "s"))>var<#else>val</#if> ${prefix}Query = <@str content=config.query/>
<#if (config.query?matches(".*\\$\\{.*?}.*", "s"))>
${prefix}Query = VariableUtils.replaceVariables(${prefix}Query, ${prefix}Variables.asJava)
</#if>
val ${prefix}Datasource = getDatasource("${config.datasource}")
val ${prefix}DF = spark.read.format("jdbc")
  .option("url", ${prefix}Datasource.getUrl)
  .option("driver", ${prefix}Datasource.getDriver)
  .option("user", ${prefix}Datasource.getUser)
  .option("password", ${prefix}Datasource.getPassword)
<#assign keys = config.options?keys>
<#list keys as key>
  .option("${key}", "${config.options[key]}")
</#list>
  .option("query", ${prefix}Query)
  .load()
${prefix}DF.createOrReplaceTempView("${config.resultTable}")
<#if (config.persist)>
${prefix}DF.persist(StorageLevel.${config.storageLevel})
</#if>

