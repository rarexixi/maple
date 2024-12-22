package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.{Dataset, Row, SparkSession}

import java.util.concurrent.atomic.AtomicInteger

trait MaplePlugin[T] extends Serializable {
  protected var config: T = _
  protected var spark: SparkSession = _

  private val counter = new AtomicInteger(0)

  def getConfig: T = config

  def setConfig(config: T): Unit = {
    this.config = config
  }

  def setSpark(spark: SparkSession): Unit = {
    this.spark = spark
  }

  def execute(variables: java.util.Map[String, String]): Unit = {
    if (counter.getAndAdd(1) > 0) {
      throw new IllegalStateException("MaplePlugin can only be executed once.")
    }
    exec(variables)
  }

  protected def exec(variables: java.util.Map[String, String]): Unit

  protected def getData(variables: java.util.Map[String, String]): Dataset[Row]

  def clean(): Unit = {
    if (counter.get() <= 1) {
      cleanUp()
    }
  }

  protected def cleanUp(): Unit = {
  }
}