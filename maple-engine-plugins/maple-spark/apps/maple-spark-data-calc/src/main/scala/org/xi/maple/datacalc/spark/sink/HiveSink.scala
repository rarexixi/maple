package org.xi.maple.datacalc.spark.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSink
import org.xi.maple.datacalc.spark.model.sink.HiveSinkConfig
import org.xi.maple.datacalc.spark.util.HiveSinkUtils

class HiveSink extends MapleSink[HiveSinkConfig] {

  override protected def exec(variables: java.util.Map[String, String]): Unit = {
    val ds: Dataset[Row] = getData(variables)
    val targetTable = config.getTargetTable.getTableIdentifierWithDb
    val targetFields = spark.table(targetTable).schema.fields
    if (config.getWriteAsFile != null && config.getWriteAsFile) {
      val partitionsColumns = spark.catalog.listColumns(targetTable)
        .where(col("isPartition") === true)
        .select("name")
        .collect()
        .map(_.getAs[String]("name"))
      val location = HiveSinkUtils.getLocation(spark, targetTable, partitionsColumns, variables)
      val fileFormat = HiveSinkUtils.getTableFileFormat(spark, targetTable)

      logger.info(s"Write $fileFormat into target table: $targetTable, location: $location, file format: $fileFormat")
      val targetFieldsWithoutPartition = targetFields.filter(field => !partitionsColumns.contains(field.name))
      val writer = HiveSinkUtils.getSaveWriter(ds, targetFieldsWithoutPartition, targetTable,
        config.getStrongCheck, config.getSaveMode, config.getOptions)
      fileFormat match {
        case HiveSinkUtils.FileFormat.PARQUET => writer.parquet(location)
        case HiveSinkUtils.FileFormat.ORC => writer.orc(location)
        case _ =>
      }

      val partition = partitionsColumns.map(colName => s"$colName='${variables.get(colName)}'").mkString(",")
      if (StringUtils.isNotBlank(partition)) {
        logger.info(s"Refresh table partition: $partition")
        HiveSinkUtils.refreshPartition(spark, targetTable, partition)
      }
    } else {
      val writer = HiveSinkUtils.getSaveWriter(ds, targetFields, targetTable,
        config.getStrongCheck, config.getSaveMode, config.getOptions)
      logger.info(s"InsertInto data to hive table: $targetTable")
      writer.format("hive").insertInto(targetTable)
    }
  }
}

