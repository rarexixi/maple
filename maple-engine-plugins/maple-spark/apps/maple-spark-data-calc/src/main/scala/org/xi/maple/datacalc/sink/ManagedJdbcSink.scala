package org.xi.maple.datacalc.sink

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSink
import org.xi.maple.datacalc.exception.DatasourceNotConfigException
import org.xi.maple.datacalc.model.NamedDatasource
import org.xi.maple.datacalc.service.NamedDatasourceService
import org.xi.maple.datacalc.util.VariableUtils

import java.util.stream.Collectors

class ManagedJdbcSink extends MapleSink[ManagedJdbcSinkConfig] {
  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setPreQueries(config.getPreQueries.stream().map(query => VariableUtils.replaceVariables(query, variables)).collect(Collectors.toList))
  }

  override def output(spark: SparkSession, ds: Dataset[Row]): Unit = {
    val datasource: NamedDatasource = NamedDatasourceService.getDatasource(config.getTargetDatasource)
    if (datasource == null) {
      throw new DatasourceNotConfigException(s"Datasource ${config.getTargetDatasource} is not configured!")
    }

    val jdbcConfig = new JdbcSinkConfig()
    jdbcConfig.setUrl(datasource.getUrl)
    jdbcConfig.setDriver(datasource.getDriver)
    jdbcConfig.setUser(datasource.getUser)
    jdbcConfig.setPassword(datasource.getPassword)
    jdbcConfig.setTargetDatabase(config.getTargetDatabase)
    jdbcConfig.setTargetTable(config.getTargetTable)
    jdbcConfig.setSaveMode(config.getSaveMode)
    jdbcConfig.setPreQueries(config.getPreQueries)
    jdbcConfig.setNumPartitions(config.getNumPartitions)
    jdbcConfig.setOptions(config.getOptions)
    jdbcConfig.setSourceTable(config.getSourceTable)
    jdbcConfig.setSourceQuery(config.getSourceQuery)

    val sinkPlugin = new JdbcSink()
    sinkPlugin.setConfig(jdbcConfig)
    sinkPlugin.output(spark, ds)
  }
}
