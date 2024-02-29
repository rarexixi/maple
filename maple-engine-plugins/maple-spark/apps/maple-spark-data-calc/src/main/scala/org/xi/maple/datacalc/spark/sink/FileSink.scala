package org.xi.maple.datacalc.spark.sink

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSink

import scala.collection.JavaConverters._

class FileSink extends MapleSink[FileSinkConfig] {

  val defaultUriSchema = "hdfs://"

  override protected def prepare(): Unit = {
    config.setPath(VariableUtils.replaceVariables(config.getPath, variables))
  }

  override def output(ds: Dataset[Row]): Unit = {

    val writer = ds.write.mode(config.getSaveMode)

    if (config.getPartitionBy != null && !config.getPartitionBy.isEmpty) {
      val partitionKeys = config.getPartitionBy.asScala
      writer.partitionBy(partitionKeys: _*)
    }

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      writer.options(config.getOptions)
    }
    val path = if (config.getPath.startsWith("/")) {
      defaultUriSchema + config.getPath
    } else {
      config.getPath
    }
    logger.info(s"Save data to file, path: $path")

    config.getSerializer match {
      case "csv" => writer.csv(path)
      case "json" => writer.json(path)
      case "parquet" => writer.parquet(path)
      case "text" => writer.text(path)
      case "orc" => writer.orc(path)
      case _ => writer.format(config.getSerializer).save(path)
    }
  }
}
