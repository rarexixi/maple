package org.xi.maple.datacalc.spark.transform

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleTransform

class SqlTransform extends MapleTransform[SqlTransformConfig] {

  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setSql(VariableUtils.replaceVariables(config.getSql, variables))
  }

  override def process(spark: SparkSession, ds: Dataset[Row]): Dataset[Row] = {
    logger.info(s"Load data from query: ${config.getSql}")
    spark.sql(config.getSql)
  }
}