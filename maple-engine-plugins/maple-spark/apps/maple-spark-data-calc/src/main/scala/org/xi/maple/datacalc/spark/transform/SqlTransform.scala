package org.xi.maple.datacalc.spark.transform

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleTransform
import org.xi.maple.datacalc.spark.model.transform.SqlTransformConfig

class SqlTransform extends MapleTransform[SqlTransformConfig] {

  override protected def getData(variables: java.util.Map[String, String]): Dataset[Row] = {
    val selectSql = VariableUtils.replaceVariables(config.getSql, variables)
    logger.info(s"Load data from query: $selectSql")
    spark.sql(selectSql)
  }
}