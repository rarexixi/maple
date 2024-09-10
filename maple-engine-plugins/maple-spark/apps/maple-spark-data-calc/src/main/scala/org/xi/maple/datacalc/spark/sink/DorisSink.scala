package org.xi.maple.datacalc.spark.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.api.MapleSink

import scala.collection.JavaConverters._

/**
 * https://doris.apache.org/zh-CN/docs/ecosystem/spark-doris-connector#%E5%86%99%E5%85%A5
 */
class DorisSink extends MapleSink[DorisSinkConfig] {
  override protected def prepare(): Unit = {
  }

  override def output(ds: Dataset[Row]): Unit = {
    val targetTable = config.getTargetDatabase + "." + config.getTargetTable
    var options = Map(
      "doris.fenodes" -> config.getFenodes,
      "user" -> config.getUser,
      "password" -> config.getPassword,
      "doris.table.identifier" -> targetTable
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    val writer = ds.write.format("doris")
    if (StringUtils.isNotBlank(config.getSaveMode)) {
      writer.mode(config.getSaveMode)
    }
    logger.info(s"Save data to doris fenodes: ${config.getFenodes}, username: ${config.getUser}, table: $targetTable")
    writer.options(options).save()
  }
}
