package org.xi.maple.transform

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleTransform

class SqlTransform extends MapleTransform[SqlTransformConfig] {

  override def process(spark: SparkSession, ds: Dataset[Row]): Dataset[Row] = {
    logger.info(s"Load data from query: ${config.getSql}")
    spark.sql(config.getSql)
  }
}