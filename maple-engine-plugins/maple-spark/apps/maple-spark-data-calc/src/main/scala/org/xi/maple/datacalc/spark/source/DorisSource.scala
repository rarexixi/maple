package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSource
import org.xi.maple.datacalc.spark.model.source.DorisSourceConfig

import scala.collection.JavaConverters._

/**
 * https://doris.apache.org/zh-CN/docs/ecosystem/spark-doris-connector#%E8%AF%BB%E5%8F%96
 */
class DorisSource extends MapleSource[DorisSourceConfig] {

  override def getData(variables: java.util.Map[String, String]): Dataset[Row] = {

    val sourceTable = config.getSourceTable.getTableIdentifierWithDb
    logger.info(s"Load data from doris fenodes: ${config.getFenodes}, table: $sourceTable, username: ${config.getUser}")

    var options = Map(
      "doris.fenodes" -> config.getFenodes,
      "user" -> config.getUser,
      "password" -> config.getPassword,
      "doris.table.identifier" -> sourceTable
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    spark.read.format("doris").options(options).load()
  }
}
