<#include "../includes/variables.ftl">
val ${prefix}Url = "${config.url}"
val ${prefix}User = "${config.user}"
val ${prefix}Password = "${config.password}"
<#if (config.preQueries?size > 0)>
val ${prefix}PreQueries = Seq(
<#list config.preQueries as query>
<@str_nowrap content=query/><#sep>,</#sep>
</#list>
)
executeQueries(${prefix}Url, ${prefix}User, ${prefix}Password, ${prefix}PreQueries)
</#if>
<#include "./includes/source_df.ftl">
  .write.format("jdbc")
  .option("url", ${prefix}Url)
  .option("driver", "${config.driver}")
  .option("user", ${prefix}User)
  .option("password", ${prefix}Password)
  .option("dbtable", "${config.targetDatabase}.${config.targetTable}")
<#assign keys = config.options?keys>
<#list keys as key>
  .option("${key}", "${config.options[key]}")
</#list>
  .mode("${config.saveMode}")
  .save()

