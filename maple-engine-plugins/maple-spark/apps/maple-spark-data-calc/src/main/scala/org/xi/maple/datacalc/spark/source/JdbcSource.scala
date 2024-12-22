package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSource
import org.xi.maple.datacalc.spark.model.source.JdbcSourceConfig

import scala.collection.JavaConverters._

class JdbcSource extends MapleSource[JdbcSourceConfig] {

  override def getData(variables: java.util.Map[String, String]): Dataset[Row] = {
    val sourceTable = config.getSourceTable.getTableIdentifierWithDb
    logger.info(s"Load data from jdbc url: ${config.getUrl}, table: $sourceTable, username: ${config.getUser}")

    var options = Map(
      "url" -> config.getUrl,
      "driver" -> config.getDriver,
      "user" -> config.getUser,
      "password" -> config.getPassword,
      "dbtable" -> sourceTable,
      "connectionCollation" -> "utf8mb4_unicode_ci"
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    spark.read.format("jdbc").options(options).load()
  }
}
