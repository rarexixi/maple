package org.xi.maple.datacalc.spark.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSink

import scala.collection.JavaConverters._

/**
 * https://docs.starrocks.io/zh/docs/loading/Spark-connector-starrocks/
 */
class StarRocksSink extends MapleSink[StarRocksSinkConfig] {
  override protected def prepare(): Unit = {
  }

  override def output(ds: Dataset[Row]): Unit = {
    val targetTable = config.getTargetDatabase + "." + config.getTargetTable
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
