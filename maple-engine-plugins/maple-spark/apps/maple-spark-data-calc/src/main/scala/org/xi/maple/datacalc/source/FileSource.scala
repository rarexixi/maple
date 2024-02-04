package org.xi.maple.datacalc.source

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSource
import org.xi.maple.datacalc.util.VariableUtils

class FileSource extends MapleSource[FileSourceConfig] {

  val defaultUriSchema = "hdfs://"

  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setPath(VariableUtils.replaceVariables(config.getPath, variables))
  }

  override def getData(spark: SparkSession): Dataset[Row] = {
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
