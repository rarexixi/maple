<#if (config.sourceQuery?length > 0)>
<#if (config.sourceQuery?matches(".*\\$\\{.*?}.*", "s"))>
var ${prefix}Query = <@str content=config.sourceQuery/>
${prefix}Query = VariableUtils.replaceVariables(${prefix}Query, ${prefix}Variables.asJava)
<#else>
val ${prefix}Query = <@str content=config.sourceQuery/>
</#if>
spark.sql(${prefix}Query)<#if (config.numPartitions > 0)>.repartition(${config.numPartitions})</#if>
<#else>
spark.read.table("${config.sourceTable}")<#if (config.numPartitions > 0)>.repartition(${config.numPartitions})</#if>
</#if>