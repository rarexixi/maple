package org.xi.maple.datacalc.spark.transform

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleTransform

class SqlTransform extends MapleTransform[SqlTransformConfig] {

  override protected def prepare(): Unit = {
    config.setSql(VariableUtils.replaceVariables(config.getSql, variables))
  }

  override protected def process(ds: Dataset[Row]): Dataset[Row] = {
    logger.info(s"Load data from query: ${config.getSql}")
    spark.sql(config.getSql)
  }
}