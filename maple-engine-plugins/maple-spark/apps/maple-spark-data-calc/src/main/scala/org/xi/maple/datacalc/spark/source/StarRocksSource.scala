package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSource
import org.xi.maple.datacalc.spark.model.source.StarRocksSourceConfig

import scala.collection.JavaConverters._

/**
 * https://docs.starrocks.io/zh/docs/unloading/Spark_connector/
 */
class StarRocksSource extends MapleSource[StarRocksSourceConfig] {

  override def getData(variables: java.util.Map[String, String]): Dataset[Row] = {
    val sourceTable = config.getSourceTable.getTableIdentifierWithDb
    logger.info(s"Load data from starrocks http url: ${config.getFeHttpUrl}, jdbc url: ${config.getFeJdbcUrl}, username: ${config.getUser}, table: $sourceTable")

    var options = Map(
      "starrocks.fe.http.url" -> config.getFeHttpUrl,
      "starrocks.fe.jdbc.url" -> config.getFeJdbcUrl,
      "starrocks.user" -> config.getUser,
      "starrocks.password" -> config.getPassword,
      "starrocks.table.identifier" -> sourceTable
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    spark.read.format("starrocks").options(options).load()
  }
}
