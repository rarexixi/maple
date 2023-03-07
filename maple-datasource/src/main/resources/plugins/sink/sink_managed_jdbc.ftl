<#include "../includes/variables.ftl">
val ${prefix}Datasource = getDatasource("${config.targetDatasource}")
<#if (config.preQueries?size > 0)>
val ${prefix}PreQueries = Seq(
<#list config.preQueries as query>
<@str_nowrap content=query/><#sep>,</#sep>
</#list>
)
executeDatasourceQueries(${prefix}Datasource, ${prefix}PreQueries)
</#if>
<#include "./includes/source_df.ftl">
  .write.format("jdbc")
  .option("url", ${prefix}Datasource.getUrl)
  .option("driver", ${prefix}Datasource.getDriver)
  .option("user", ${prefix}Datasource.getUser)
  .option("password", ${prefix}Datasource.getPassword)
  .option("dbtable", "${config.targetDatabase}.${config.targetTable}")
<#assign keys = config.options?keys>
<#list keys as key>
  .option("${key}", "${config.options[key]}")
</#list>
  .mode("${config.saveMode}")
  .save()

