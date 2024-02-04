package org.xi.maple.datacalc.source

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.api.MapleSource
import org.xi.maple.datacalc.exception.DatasourceNotConfigException
import org.xi.maple.datacalc.service.NamedDatasourceService
import org.xi.maple.datacalc.util.VariableUtils

class ManagedJdbcSource extends MapleSource[ManagedJdbcSourceConfig] {

  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setQuery(VariableUtils.replaceVariables(config.getQuery, variables))
  }

  override def getData(spark: SparkSession): Dataset[Row] = {
    val datasource = NamedDatasourceService.getDatasource(config.getDatasource)
    if (datasource == null) {
      throw new DatasourceNotConfigException(s"Datasource ${config.getDatasource} is not configured!")
    }

    val jdbcConfig = new JdbcSourceConfig()
    jdbcConfig.setUrl(datasource.getUrl)
    jdbcConfig.setDriver(datasource.getDriver)
    jdbcConfig.setUser(datasource.getUser)
    jdbcConfig.setPassword(datasource.getPassword)
    jdbcConfig.setQuery(config.getQuery)
    jdbcConfig.setTable(config.getTable)
    jdbcConfig.setPersist(config.getPersist)
    jdbcConfig.setStorageLevel(config.getStorageLevel)
    jdbcConfig.setOptions(config.getOptions)
    jdbcConfig.setResultTable(config.getResultTable)

    val sourcePlugin = new JdbcSource()
    sourcePlugin.setConfig(jdbcConfig)
    sourcePlugin.getData(spark)
  }
}
