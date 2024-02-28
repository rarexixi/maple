package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.spark.model.SourceConfig

trait MapleSource[T <: SourceConfig] extends MaplePlugin[T] with Logging {
  def getData(spark: SparkSession): Dataset[Row]
}
