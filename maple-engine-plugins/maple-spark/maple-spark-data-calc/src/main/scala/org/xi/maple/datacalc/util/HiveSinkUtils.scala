package org.xi.maple.datacalc.util

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.catalog.HiveTableRelation
import org.apache.spark.sql.execution.datasources.{HadoopFsRelation, LogicalRelation}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.sources.DataSourceRegister
import org.apache.spark.sql.types.StructField
import org.slf4j.{Logger, LoggerFactory}
import org.xi.maple.datacalc.exception.HiveSinkException

object HiveSinkUtils {

  private val log: Logger = LoggerFactory.getLogger(HiveSinkUtils.getClass)

  def logFields(sourceFields: Array[StructField], targetFields: Array[StructField]): Unit = {
    log.info(s"sourceFields: ${sourceFields.mkString("Array(", ", ", ")")}")
    log.info(s"targetFields: ${targetFields.mkString("Array(", ", ", ")")}")
  }

  def getSaveWriter(ds: Dataset[Row], targetFields: Array[StructField], targetTable: String, strongCheck: Boolean, saveMode: String, options: java.util.Map[String, String]): DataFrameWriter[Row] = {
    val dsSource = sequenceFields(ds, ds.schema.fields, targetFields, targetTable)
    val sourceFields = dsSource.schema.fields

    // 开启强校验时，校验字段类型是否匹配
    if (strongCheck) {
      for (i <- sourceFields.indices) {
        val targetField = targetFields(i)
        val sourceField = sourceFields(i)
        if (!targetField.dataType.equals(sourceField.dataType)) {
          logFields(sourceFields, targetFields)
          throw new HiveSinkException(s"${i + 1}st column (${sourceField.name}[${sourceField.dataType}]) name or data type does not match target table column (${targetField.name}[${targetField.dataType}])")
        }
      }
    }

    val writer = dsSource.write.mode(saveMode)
    if (options.isEmpty) {
      writer.options(options)
    }
    writer
  }

  def sequenceFields(dsSource: Dataset[Row], sourceFields: Array[StructField], targetFields: Array[StructField], targetTable: String): DataFrame = {
    if (targetFields.length != sourceFields.length) {
      logFields(sourceFields, targetFields)
      throw new HiveSinkException(s"$targetTable requires that the data to be inserted have the same number of columns as the target table: target table has ${targetFields.length} column(s) but the inserted data has ${sourceFields.length} column(s)")
    }

    // 这里字段名都转小写，spark 元数据的字段里有大小写混合的，但hive的都是小写
    val sourceFieldMap = sourceFields.map(field => field.name.toLowerCase -> field).toMap
    val targetFieldMap = targetFields.map(field => field.name.toLowerCase -> field).toMap

    val subSet = targetFieldMap.keySet -- sourceFieldMap.keySet
    if (subSet.isEmpty) {
      // 重排字段顺序，防止字段顺序不一致
      dsSource.select(targetFields.map(field => col(field.name)): _*)
    } else if (subSet.size == targetFieldMap.size) {
      // 字段名都无法对应时，字段按顺序重命名为目标表字段
      log.info("None target table fields match with source fields, write in order")
      dsSource.toDF(targetFields.map(field => field.name): _*)
    } else {
      throw new HiveSinkException(s"$targetTable fields(${subSet.mkString(",")}) are not exist in source fields")
    }
  }

  /**
   * 获取hive表存储位置
   *
   * @param spark
   * @param targetTable 表名 database.table
   * @return 表路径
   */
  def getLocation(spark: SparkSession, targetTable: String, partitionsColumns: Array[String], variables: Map[String, String]): String = {
    val locations = spark.sql(s"desc formatted $targetTable").filter(col("col_name") === "Location").collect()
    var location: String = locations(0).getString(1)
    for (partitionColName <- partitionsColumns) {
      if (StringUtils.isBlank(variables.getOrElse(partitionColName, ""))) {
        throw new HiveSinkException(s"Please set [${partitionsColumns.mkString(", ")}] in variables")
      }
      location += s"/$partitionColName=${variables.get(partitionColName)}"
    }
    location
  }

  def getTableFileFormat(spark: SparkSession, targetTable: String): FileFormat.Value = {
    try {
      var fileFormat: FileFormat.FileFormat = FileFormat.OTHER
      spark.table(targetTable).queryExecution.optimizedPlan match {
        case logicalRelation: LogicalRelation =>
          logicalRelation.relation match {
            case hadoopFsRelation: HadoopFsRelation =>
              hadoopFsRelation.fileFormat match {
                case _: org.apache.spark.sql.execution.datasources.orc.OrcFileFormat => fileFormat = FileFormat.ORC
                case _: org.apache.spark.sql.execution.datasources.parquet.ParquetFileFormat => fileFormat = FileFormat.PARQUET
                case dataSourceRegister: DataSourceRegister => fileFormat = FileFormat.withName(dataSourceRegister.shortName.toUpperCase)
                case _ =>
              }
          }
        case hiveTableRelation: HiveTableRelation =>
        // todo
      }
      fileFormat
    } catch {
      case _: Exception => FileFormat.OTHER
    }
  }

  def refreshPartition(spark: SparkSession, targetTable: String, partition: String): Unit = {
    spark.sql(s"ALTER TABLE $targetTable DROP IF EXISTS partition($partition)")
    spark.sql(s"ALTER TABLE $targetTable ADD IF NOT EXISTS partition($partition)")
  }

  object FileFormat extends Enumeration {
    type FileFormat = Value
    val ORC, PARQUET, OTHER = Value
  }
}

