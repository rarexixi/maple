package org.xi.maple.spark3

import org.apache.parquet.format.event.Consumers.Consumer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.util.concurrent.CountDownLatch

object MapleApp {

  private var spark: SparkSession = _
  private val latch = new CountDownLatch(1)

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    spark = SparkSession.builder.config(sparkConf).getOrCreate()

    latch.await()
    spark.close()
  }

  def close(): Unit = {
    latch.countDown();
  }

  def execResult(jobId: Int, sql: String): Any = {
    try {
      val sc = spark.sparkContext
      sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
      val df = spark.sql(sql)
      val result = Map(
        "schema" -> df.schema.fields,
        "data" -> df.collect().map(row => row.toSeq)
      )
      sc.clearJobGroup()
      return result
    } catch {
      case t: Throwable => t.printStackTrace()
    }
    null
  }

  def exec(jobId: Int, sql: String, consumer: Consumer[String]): Unit = {
    try {
      val sc = spark.sparkContext
      sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
      val df = spark.sql(sql)
      df.write.mode("overwrite").parquet("")
    } catch {
      case t: Throwable => t.printStackTrace()
    }
  }
}
