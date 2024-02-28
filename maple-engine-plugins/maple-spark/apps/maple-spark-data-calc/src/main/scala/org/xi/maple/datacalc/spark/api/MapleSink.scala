package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.spark.model.SinkConfig

trait MapleSink[T <: SinkConfig] extends MaplePlugin[T] with Logging {
  def output(spark: SparkSession, data: Dataset[Row]): Unit
}