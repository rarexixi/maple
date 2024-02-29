package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.datacalc.spark.model.TransformConfig

trait MapleTransform[T <: TransformConfig] extends MapleResultTablePlugin[T] with Logging {

  override protected def getData: Dataset[Row] = {
    val fromDs = spark.read.table(config.getSourceTable)
    process(fromDs)
  }

  protected def process(ds: Dataset[Row]): Dataset[Row]
}

