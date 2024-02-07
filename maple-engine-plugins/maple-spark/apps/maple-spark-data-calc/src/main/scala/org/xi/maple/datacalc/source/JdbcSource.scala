package org.xi.maple.datacalc.source

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.api.MapleSource

class JdbcSource extends MapleSource[JdbcSourceConfig] {

  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setQuery(VariableUtils.replaceVariables(config.getQuery, variables))
  }

  override def getData(spark: SparkSession): Dataset[Row] = {
    val reader = spark.read.format("jdbc")
    if (config.getOptions != null && !config.getOptions.isEmpty) {
      reader.options(config.getOptions)
    }

    if (StringUtils.isNotBlank(config.getQuery)) {
      logger.info(s"Load data from jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, query: ${config.getQuery}")
      reader.option("query", config.getQuery)
    } else {
      logger.info(s"Load data from jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, query: ${config.getTable}")
      reader.option("dbtable", config.getTable)
    }

    reader.option("url", config.getUrl)
      .option("driver", config.getDriver)
      .option("user", config.getUser)
      .option("password", config.getPassword)
      .option("query", config.getQuery).load()
  }
}
