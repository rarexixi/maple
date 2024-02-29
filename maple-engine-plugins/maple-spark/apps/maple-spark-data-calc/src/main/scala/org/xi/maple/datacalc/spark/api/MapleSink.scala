package org.xi.maple.datacalc.spark.api

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.model.SinkConfig

trait MapleSink[T <: SinkConfig] extends MaplePlugin[T] with Logging {

  override protected def exec(): Unit = {
    val ds: Dataset[Row] = if (StringUtils.isBlank(config.getSourceQuery)) {
      spark.read.table(config.getSourceTable)
    } else {
      spark.sql(config.getSourceQuery)
    }
    val partitions = config.getNumPartitions
    if (partitions != null && partitions > 0) {
      output(ds.repartition(partitions))
    } else {
      output(ds)
    }
  }

  def output(ds: Dataset[Row]): Unit
}