package org.xi.maple.sink

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSink
import org.xi.maple.exception.DatasourceNotConfigException
import org.xi.maple.model.NamedDatasource
import org.xi.maple.service.NamedDatasourceService

class ManagedJdbcSink extends MapleSink[ManagedJdbcSinkConfig] {

  def output(spark: SparkSession, ds: Dataset[Row]): Unit = {
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
    jdbcConfig.setVariables(config.getVariables)

    val sinkPlugin = new JdbcSink()
    sinkPlugin.setConfig(jdbcConfig)
    sinkPlugin.output(spark, ds)
  }
}
