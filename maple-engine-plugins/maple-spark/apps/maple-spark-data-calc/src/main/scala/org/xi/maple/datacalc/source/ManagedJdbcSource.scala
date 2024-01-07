package org.xi.maple.datacalc.source

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.api.MapleSource
import org.xi.maple.datacalc.exception.DatasourceNotConfigException
import org.xi.maple.datacalc.service.NamedDatasourceService

class ManagedJdbcSource extends MapleSource[ManagedJdbcSourceConfig] {

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
    jdbcConfig.setPersist(config.getPersist)
    jdbcConfig.setStorageLevel(config.getStorageLevel)
    jdbcConfig.setOptions(config.getOptions)
    jdbcConfig.setResultTable(config.getResultTable)
    jdbcConfig.setVariables(config.getVariables)

    val sourcePlugin = new JdbcSource()
    sourcePlugin.setConfig(jdbcConfig)
    sourcePlugin.getData(spark)
  }
}
