package org.xi.maple.datacalc.spark.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSink
import org.xi.maple.datacalc.spark.model.sink.StarRocksSinkConfig

import scala.collection.JavaConverters._

/**
 * https://docs.starrocks.io/zh/docs/loading/Spark-connector-starrocks/
 */
class StarRocksSink extends MapleSink[StarRocksSinkConfig] {

  override protected def exec(variables: java.util.Map[String, String]): Unit = {
    val ds: Dataset[Row] = getData(variables)
    val targetTable = config.getTargetTable.getTableIdentifierWithDb
    var options = Map(
      "starrocks.fe.http.url" -> config.getFeHttpUrl,
      "starrocks.fe.jdbc.url" -> config.getFeJdbcUrl,
      "starrocks.user" -> config.getUser,
      "starrocks.password" -> config.getPassword,
      "starrocks.table.identifier" -> targetTable
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    val writer = ds.write.format("starrocks")
    if (StringUtils.isNotBlank(config.getSaveMode)) {
      writer.mode(config.getSaveMode)
    }
    logger.info(s"Save data to starrocks http url: ${config.getFeHttpUrl}, jdbc url: ${config.getFeJdbcUrl}, table: $targetTable, username: ${config.getUser}")
    writer.options(options).save()
  }
}
