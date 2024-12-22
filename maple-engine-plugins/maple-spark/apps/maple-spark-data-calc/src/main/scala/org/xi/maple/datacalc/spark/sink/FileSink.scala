package org.xi.maple.datacalc.spark.sink

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSink
import org.xi.maple.datacalc.spark.model.sink.FileSinkConfig

import scala.collection.JavaConverters._

class FileSink extends MapleSink[FileSinkConfig] {

  val defaultUriSchema = "hdfs://"

  override protected def exec(variables: java.util.Map[String, String]): Unit = {
    val ds: Dataset[Row] = getData(variables)

    val writer = ds.write.mode(config.getSaveMode)

    if (config.getPartitionBy != null && !config.getPartitionBy.isEmpty) {
      val partitionKeys = config.getPartitionBy.asScala
      writer.partitionBy(partitionKeys: _*)
    }

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      writer.options(config.getOptions)
    }
    val path = if (config.getPath.startsWith("/")) {
      defaultUriSchema + VariableUtils.replaceVariables(config.getPath, variables)
    } else {
      VariableUtils.replaceVariables(config.getPath, variables)
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
