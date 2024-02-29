package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSource

class FileSource extends MapleSource[FileSourceConfig] {

  val defaultUriSchema = "hdfs://"

  override protected def prepare(): Unit = {
    config.setPath(VariableUtils.replaceVariables(config.getPath, variables))
  }

  override def getData: Dataset[Row] = {
    val reader = spark.read

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      reader.options(config.getOptions)
    }
    val path = if (config.getPath.startsWith("/")) {
      defaultUriSchema + config.getPath
    } else {
      config.getPath
    }

    logger.info(s"Load data from file <$path>")

    var df = config.getSerializer match {
      case "csv" => reader.csv(path)
      case "json" => reader.json(path)
      case "parquet" => reader.parquet(path)
      case "text" => reader.text(path)
      case "orc" => reader.orc(path)
      case _ => reader.format(config.getSerializer).load(path)
    }
    if (config.getColumnNames != null && config.getColumnNames.length > 0) {
      df = df.toDF(config.getColumnNames: _*)
    }
    df
  }
}
