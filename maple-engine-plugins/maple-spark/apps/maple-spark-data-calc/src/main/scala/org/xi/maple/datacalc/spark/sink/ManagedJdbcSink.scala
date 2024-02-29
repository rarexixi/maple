package org.xi.maple.datacalc.spark.sink

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSink
import org.xi.maple.datacalc.spark.exception.DatasourceNotConfigException
import org.xi.maple.datacalc.spark.model.NamedDatasource
import org.xi.maple.datacalc.spark.service.NamedDatasourceService

import scala.collection.JavaConverters._

class ManagedJdbcSink extends MapleSink[ManagedJdbcSinkConfig] {
  override protected def prepare(): Unit = {
    config.setPreQueries(config.getPreQueries.asScala.map(query => VariableUtils.replaceVariables(query, variables)).asJava)
  }

  override def output(ds: Dataset[Row]): Unit = {
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
    sinkPlugin.output(ds)
  }
}
