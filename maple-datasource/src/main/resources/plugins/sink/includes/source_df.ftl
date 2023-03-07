<#if (config.sourceQuery?length > 0)>
<#if (config.sourceQuery?matches(".*\\$\\{.*?}.*", "s"))>
var ${prefix}Query = <@str content=config.sourceQuery/>
${prefix}Query = VariableUtils.replaceVariables(${prefix}Query, ${prefix}Variables.asJava)
<#else>
val ${prefix}Query = <@str content=config.sourceQuery/>
</#if>
spark.sql(${prefix}Query)
<#else>
spark.read.table("${config.sourceTable}")
</#if>