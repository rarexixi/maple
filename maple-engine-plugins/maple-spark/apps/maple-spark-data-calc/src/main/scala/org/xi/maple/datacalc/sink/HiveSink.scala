package org.xi.maple.datacalc.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSink
import org.xi.maple.datacalc.util.HiveSinkUtils

import scala.collection.JavaConverters.mapAsScalaMapConverter

class HiveSink extends MapleSink[HiveSinkConfig] {

  def output(spark: SparkSession, ds: Dataset[Row]): Unit = {
    val targetTable = config.getTargetDatabase + "." + config.getTargetTable
    val targetFields = spark.table(targetTable).schema.fields
    val dsSource = if (config.getNumPartitions > 0) {
      ds.repartition(config.getNumPartitions)
    } else {
      ds
    }
    if (config.getWriteAsFile != null && config.getWriteAsFile) {
      val partitionsColumns = spark.catalog.listColumns(targetTable)
        .where(col("isPartition") === true)
        .select("name")
        .collect()
        .map(_.getAs[String]("name"))
      val location = HiveSinkUtils.getLocation(spark, targetTable, partitionsColumns, config.getVariables.asScala.toMap)
      val fileFormat = HiveSinkUtils.getTableFileFormat(spark, targetTable)

      logger.info(s"Write $fileFormat into target table: $targetTable, location: $location, file format: $fileFormat")
      val targetFieldsWithoutPartition = targetFields.filter(field => !partitionsColumns.contains(field.name))
      val writer = HiveSinkUtils.getSaveWriter(dsSource, targetFieldsWithoutPartition, targetTable,
        config.getStrongCheck, config.getSaveMode, config.getOptions)
      fileFormat match {
        case HiveSinkUtils.FileFormat.PARQUET => writer.parquet(location)
        case HiveSinkUtils.FileFormat.ORC => writer.orc(location)
        case _ =>
      }

      val partition = partitionsColumns.map(colName => s"$colName='${config.getVariables.get(colName)}'").mkString(",")
      if (StringUtils.isNotBlank(partition)) {
        logger.info(s"Refresh table partition: $partition")
        HiveSinkUtils.refreshPartition(spark, targetTable, partition)
      }
    } else {
      val writer = HiveSinkUtils.getSaveWriter(dsSource, targetFields, targetTable,
        config.getStrongCheck, config.getSaveMode, config.getOptions)
      logger.info(s"InsertInto data to hive table: $targetTable")
      writer.format("hive").insertInto(targetTable)
    }
  }
}

