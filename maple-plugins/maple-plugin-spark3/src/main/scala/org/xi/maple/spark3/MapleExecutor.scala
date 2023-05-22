package org.xi.maple.spark3

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods

import java.util.UUID

object MapleExecutor {

  def execJsonResult(spark: SparkSession, jobId: Int, sql: String): Array[Map[String, Any]] = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    getResultFromDataFrame(spark, jobId, sql,
      df => df.collect().map((row: Row) => JsonMethods.parse(row.json).extract[Map[String, Any]]))
  }

  def execSchematicResult(spark: SparkSession, jobId: Int, sql: String): Map[String, Any] = {
    getResultFromDataFrame(spark, jobId, sql, df => Map(
      "schema" -> df.schema.fields.map(item => Map(
        "name" -> item.name,
        "type" -> item.dataType.typeName,
        "nullable" -> item.nullable
      )),
      "data" -> df.collect().map(row => row.toSeq)
    ))
  }

  def exec(spark: SparkSession, jobId: Int, sql: String): String = {
    val sc = spark.sparkContext
    sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
    val df = spark.sql(sql)
    val path = """/tmp/maple/""" + UUID.randomUUID().toString
    df.write.mode("overwrite").parquet(path)
    sc.clearJobGroup()
    path
  }

  private def getResultFromDataFrame[T](spark: SparkSession, jobId: Int, sql: String, parseResult: DataFrame => T): T = {
    val sc = spark.sparkContext
    sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
    val df = spark.sql(sql)
    val result = parseResult(df)
    println(result)
    sc.clearJobGroup()
    result
  }
}
