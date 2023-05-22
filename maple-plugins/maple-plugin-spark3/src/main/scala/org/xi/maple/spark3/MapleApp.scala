package org.xi.maple.spark3

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.ui.ExtendedApiServer

import java.util.concurrent.CountDownLatch

object MapleApp {

  private var spark: SparkSession = _
  private val latch = new CountDownLatch(1)

  def main(args: Array[String]): Unit = {

    val sparkConf = createSparkConf()
    spark = SparkSession.builder.config(sparkConf).getOrCreate()

    ExtendedApiServer.registerApis(spark)

    latch.await()
    spark.close()
  }

  def close(): Unit = {
    latch.countDown()
  }

  def execSchematicResult(jobId: Int, sql: String): Map[String, Any] = {
    MapleExecutor.execSchematicResult(spark, jobId, sql)
  }

  def execJsonResult(jobId: Int, sql: String): Array[Map[String, Any]] = {
    MapleExecutor.execJsonResult(spark, jobId, sql)
  }

  def exec(jobId: Int, sql: String): Unit = {
    MapleExecutor.exec(spark, jobId, sql)
  }

  def createSparkConf(): SparkConf = {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("maple").setMaster("local[*]")
    sparkConf.set("spark.testing.memory", "2148480000")
    sparkConf.set("driver-memory", "1g")
    sparkConf.set("executor-memory", "1500m")
    sparkConf.set("num-executors", "2")
    sparkConf.set("executor-cores", "2")
    sparkConf.set("queue", "default")
    sparkConf.set("spark.sql.catalogImplementation", "hive")
    sparkConf.set("spark.rpc.message.maxSize", "2047")
    sparkConf.set("spark.executor.memoryOverhead", "2048")
    sparkConf.set("spark.kryoserializer.buffer.max", "2047m")
    sparkConf.set("spark.driver.maxResultSize", "2g")
    sparkConf.set("spark.sql.parquet.writeLegacyFormat", "true")
    sparkConf.set("spark.sql.crossJoin.enabled", "true")
    sparkConf.set("spark.sql.codegen.wholeStage", "false")
    sparkConf.set("spark.sql.warehouse.dir", "/Users/xishihao/apps/hive/warehouse")
    sparkConf.set("hive.metastore.uris", "thrift://hive-metastore-01:9083")
    sparkConf.set("hive.exec.dynamic.partition", "true")
    sparkConf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    sparkConf.set("hive.metastore.event.db.notification.api.auth", "false")
    System.setProperty("HADOOP_USER_NAME", "hadoop")
    sparkConf
  }
}
