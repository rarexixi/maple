package org.xi.maple.datacalc.spark.api

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.model.SinkConfig

import java.util

trait MapleSink[T <: SinkConfig] extends MaplePlugin[T] with Logging {

  override protected def getData(variables: util.Map[String, String]): Dataset[Row] = {
    val ds: Dataset[Row] = if (StringUtils.isBlank(config.getSourceQuery)) {
      spark.read.table(config.getSourceTable)
    } else {
      val sourceQuery = VariableUtils.replaceVariables(config.getSourceQuery, variables)
      spark.sql(sourceQuery)
    }
    val partitions = config.getNumPartitions
    if (partitions != null && partitions > 0) {
      ds.repartition(partitions)
    } else {
      ds
    }
  }
}