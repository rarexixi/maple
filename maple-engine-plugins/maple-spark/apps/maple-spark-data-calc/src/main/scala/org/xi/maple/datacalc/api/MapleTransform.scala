package org.xi.maple.datacalc.api

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.model.TransformConfig

trait MapleTransform[T <: TransformConfig] extends MaplePlugin[T] with Logging {
  def process(spark: SparkSession, ds: Dataset[Row]): Dataset[Row]
}

