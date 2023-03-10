package org.xi.maple.api

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.model.TransformConfig

trait MapleTransform[T <: TransformConfig] extends MaplePlugin[T] with Logging {
  def process(spark: SparkSession, ds: Dataset[Row]): Dataset[Row]
}

