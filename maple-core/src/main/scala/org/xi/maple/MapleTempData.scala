package org.xi.maple

import org.apache.spark.sql.{Dataset, Row, SQLContext}

import java.util.UUID
import scala.collection.mutable

object MapleTempData {
  def randomTableName: String = {
    UUID.randomUUID().toString.replaceAll("-", "").substring(8)
  }

  val TABLE_IN_SQL_MAP: mutable.Map[String, String] = mutable.Map[String, String]()
  val RESULT_TABLES: mutable.Set[String] = mutable.Set[String]()
  val PERSIST_DATASETS: mutable.Set[Dataset[Row]] = mutable.Set[Dataset[Row]]()

  def putTempTable(tableName: String, tableAliasName: String): Unit = {
    TABLE_IN_SQL_MAP.put(tableName, tableAliasName)
  }

  def putResultTable(resultTableName: String): Unit = {
    RESULT_TABLES.add(resultTableName)
  }

  def putPersistDataSet(ds: Dataset[Row]): Unit = {
    PERSIST_DATASETS.add(ds)
  }

  def getTempTable(tableName: String): String = {
    TABLE_IN_SQL_MAP(tableName)
  }

  def containsTable(tableName: String): Boolean = {
    TABLE_IN_SQL_MAP.contains(tableName)
  }

  def clean(sqlContext: SQLContext): Unit = {
    TABLE_IN_SQL_MAP.values.foreach(tableAliasName => sqlContext.dropTempTable(tableAliasName))
    TABLE_IN_SQL_MAP.clear()

    RESULT_TABLES.foreach(resultTable => sqlContext.dropTempTable(resultTable))
    RESULT_TABLES.clear()

    PERSIST_DATASETS.foreach(ds => ds.unpersist())
    PERSIST_DATASETS.clear()
  }
}