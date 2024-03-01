package org.xi.maple.datacalc.spark.source

import org.apache.spark.sql.{Dataset, Row}
import org.xi.maple.common.util.VariableUtils
import org.xi.maple.datacalc.spark.api.MapleSource
import org.xi.maple.datacalc.spark.exception.DatasourceNotConfigException
import org.xi.maple.datacalc.spark.service.NamedDatasourceService

class ManagedJdbcSource extends MapleSource[ManagedJdbcSourceConfig] {

  override protected def prepare(): Unit = {
    config.setQuery(VariableUtils.replaceVariables(config.getQuery, variables))
  }

  override def getData: Dataset[Row] = {
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
    sourcePlugin.getData
  }
}