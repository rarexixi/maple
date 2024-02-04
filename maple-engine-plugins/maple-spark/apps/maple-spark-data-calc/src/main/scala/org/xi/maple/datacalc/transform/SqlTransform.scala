package org.xi.maple.datacalc.transform

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.api.MapleTransform
import org.xi.maple.datacalc.util.VariableUtils

class SqlTransform extends MapleTransform[SqlTransformConfig] {

  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setSql(VariableUtils.replaceVariables(config.getSql, variables))
  }

  override def process(spark: SparkSession, ds: Dataset[Row]): Dataset[Row] = {
    logger.info(s"Load data from query: ${config.getSql}")
    spark.sql(config.getSql)
  }
}