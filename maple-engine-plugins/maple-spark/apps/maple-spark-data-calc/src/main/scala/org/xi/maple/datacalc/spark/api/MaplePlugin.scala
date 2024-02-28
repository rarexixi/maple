package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.SparkSession

trait MaplePlugin[T] extends Serializable {
  protected var config: T = _

  def getConfig: T = config

  def setConfig(config: T): Unit = {
    this.config = config
  }

  def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
  }
}