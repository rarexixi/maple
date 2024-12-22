package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.storage.StorageLevel
import org.xi.maple.datacalc.spark.model.ResultTableConfig

trait MapleResultTablePlugin[T <: ResultTableConfig] extends MaplePlugin[T] with Logging {

  private var ds: Dataset[Row] = _

  override protected def exec(variables: java.util.Map[String, String]): Unit = {
    ds = getData(variables)
    if (ds == null) return
    ds.createOrReplaceTempView(config.getResultTable)
    if (config.getPersist) {
      ds.persist(StorageLevel.fromString(config.getStorageLevel))
    }
  }

  override protected def cleanUp(): Unit = {
    if (ds == null) return
    spark.sqlContext.dropTempTable(config.getResultTable)
    if (config.getPersist) {
      ds.unpersist()
    }
  }
}

