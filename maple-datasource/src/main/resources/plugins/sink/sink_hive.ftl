<#include "../includes/variables.ftl">

val ${prefix}TargetTable = "${config.targetDatabase}.${config.targetTable}"
val ${prefix}TargetFields = spark.table(targetTable).schema.fields

val ${prefix}Options = Map(
<#assign keys = config.options?keys>
<#list keys as key>
  "${key}" -> "${config.options[key]}"<#sep>,</#sep>
</#list>
)
<#if (config.sourceQuery?length > 0)>
<#if (config.sourceQuery?matches(".*\\$\\{.*?}.*", "s"))>
var ${prefix}Query = <@str content=config.sourceQuery/>
${prefix}Query = VariableUtils.replaceVariables(${prefix}Query, ${prefix}Variables.asJava)
<#else>
val ${prefix}Query = <@str content=config.sourceQuery/>
</#if>
val ${prefix}Ds = spark.sql(${prefix}Query)<#if (config.numPartitions > 0)>.repartition(${config.numPartitions})</#if>
<#else>
val ${prefix}Ds = spark.read.table("${config.sourceTable}")<#if (config.numPartitions > 0)>.repartition(${config.numPartitions})</#if>
</#if>
<#if (config.writeAsFile)>
val ${prefix}PartitionsColumns = spark.catalog.listColumns(${prefix}TargetTable)
  .where(col("isPartition") === true)
  .select("name")
  .collect()
  .map(_.getAs[String]("name"))
val ${prefix}Location = HiveSinkUtils.getLocation(spark, ${prefix}TargetTable, ${prefix}PartitionsColumns, ${prefix}Variables.asScala.toMap)
val ${prefix}FileFormat = HiveSinkUtils.getTableFileFormat(spark, ${prefix}TargetTable)

val ${prefix}Writer = HiveSinkUtils.getSaveWriter(${prefix}Ds, targetFields.filter(field => !${prefix}PartitionsColumns.contains(field.name)), ${prefix}TargetTable,
  ${config.strongCheck?then('true', 'false')}, "${config.saveMode}", ${prefix}Options, ${config.numPartitions})
${prefix}FileFormat match {
  case HiveSinkUtils.FileFormat.PARQUET => writer.parquet(${prefix}Location)
  case HiveSinkUtils.FileFormat.ORC => writer.orc(${prefix}Location")
  case _ =>
}

val ${prefix}Partition = ${prefix}PartitionsColumns.map(colName => s"$colName='${r"$"}{${prefix}Variables.get(colName)}'").mkString(",")
if (StringUtils.isNotBlank(${prefix}Partition)) {
  HiveSinkUtils.refreshPartition(spark, ${prefix}TargetTable, ${prefix}Partition)
}
<#else>
val ${prefix}Writer = HiveSinkUtils.getSaveWriter(${prefix}Ds, ${prefix}TargetFields, ${prefix}TargetTable,
  ${config.strongCheck?then('true', 'false')}, "${config.saveMode}", ${prefix}Options, ${config.numPartitions})
writer.format("hive").insertInto(${prefix}TargetTable)
</#if>

