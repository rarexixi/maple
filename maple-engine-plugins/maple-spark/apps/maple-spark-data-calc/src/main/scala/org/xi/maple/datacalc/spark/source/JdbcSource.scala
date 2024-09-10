package org.xi.maple.datacalc.spark.source

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSource

import scala.collection.JavaConverters._

class JdbcSource extends MapleSource[JdbcSourceConfig] {

  override protected def prepare(): Unit = {
    config.setQuery(VariableUtils.replaceVariables(config.getQuery, variables))
  }

  override def getData: Dataset[Row] = {

    val sourceData = if (StringUtils.isNotBlank(config.getQuery)) {
      logger.info(s"Load data from jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, query: ${config.getQuery}")
      Map("query" -> config.getQuery)
    } else {
      logger.info(s"Load data from jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, query: ${config.getTable}")
      Map("dbtable" -> config.getTable)
    }

    var options = Map(
      "url" -> config.getUrl,
      "driver" -> config.getDriver,
      "user" -> config.getUser,
      "password" -> config.getPassword,
      "connectionCollation" -> "utf8mb4_unicode_ci"
    ) ++ sourceData

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    spark.read.format("jdbc").options(options).load()
  }
}
