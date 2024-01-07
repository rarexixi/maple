package org.xi.maple.datacalc.source

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSource
import org.xi.maple.datacalc.util.VariableUtils

class JdbcSource extends MapleSource[JdbcSourceConfig] {

  override def getData(spark: SparkSession): Dataset[Row] = {
    config.setQuery(VariableUtils.replaceVariables(config.getQuery, config.getVariables))

    val reader = spark.read.format("jdbc")
    if (config.getOptions != null && !config.getOptions.isEmpty) {
      reader.options(config.getOptions)
    }

    logger.info(s"Load data from jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, query: ${config.getQuery}")

    reader.option("url", config.getUrl)
      .option("driver", config.getDriver)
      .option("user", config.getUser)
      .option("password", config.getPassword)
      .option("query", config.getQuery).load()
  }
}
