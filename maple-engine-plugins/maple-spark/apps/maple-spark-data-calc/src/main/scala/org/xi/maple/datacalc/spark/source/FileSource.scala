package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSource
import org.xi.maple.datacalc.spark.model.source.FileSourceConfig

class FileSource extends MapleSource[FileSourceConfig] {

  val defaultUriSchema = "hdfs://"

  override def getData(variables: java.util.Map[String, String]): Dataset[Row] = {
    val reader = spark.read

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      reader.options(config.getOptions)
    }
    val path = if (config.getPath.startsWith("/")) {
      defaultUriSchema + VariableUtils.replaceVariables(config.getPath, variables)
    } else {
      VariableUtils.replaceVariables(config.getPath, variables)
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
