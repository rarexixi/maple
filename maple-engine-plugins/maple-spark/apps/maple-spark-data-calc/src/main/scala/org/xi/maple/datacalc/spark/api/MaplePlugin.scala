package org.xi.maple.datacalc.spark.api

import org.apache.spark.sql.SparkSession

import java.util.concurrent.atomic.AtomicInteger

trait MaplePlugin[T] extends Serializable {
  protected var config: T = _
  protected var spark: SparkSession = _
  protected var variables: java.util.Map[String, String] = _

  private val counter = new AtomicInteger(0)

  def getConfig: T = config

  def setConfig(config: T): Unit = {
    this.config = config
  }

  def setSpark(spark: SparkSession): Unit = {
    this.spark = spark
  }

  def setVariables(variables: java.util.Map[String, String]): Unit = {
    this.variables = variables
  }

  def execute(): Unit = {
    if (counter.getAndAdd(1) > 0) {
      throw new IllegalStateException("MaplePlugin can only be executed once.")
    }
    prepare()
    exec()
  }

  protected def prepare(): Unit

  protected def exec(): Unit

  def clean(): Unit = {
    if (counter.get() <= 1) {
      cleanUp()
    }
  }

  protected def cleanUp(): Unit = {
  }
}