package org.xi.maple.datacalc.spark

import org.apache.spark.sql.SparkSession
import org.xi.maple.datacalc.spark.api.Logging
import org.xi.maple.datacalc.spark.model.{MapleArrayData, MapleData, MapleGroupData}

object MapleApp extends Logging {

  def main(args: Array[String]): Unit = {
    val argsMap = ArgsParser.getParams(args)
    val execType = argsMap.getOrElse("exec-type", "array")

    val spark = SparkSession.builder.getOrCreate()
    try {
      val config: String = if (argsMap.contains("data")) argsMap("data") else getContent(spark, argsMap("file"))
      val data: MapleData = if ("group" == execType) MapleGroupData.getData(config) else MapleArrayData.getData(config)
      val execution = new MapleExecution(spark, data, null)
      execution.execute()
    } finally {
      spark.close()
    }
  }

  private def getContent(spark: SparkSession, path: String): String = {
    val value = spark.sparkContext.wholeTextFiles(path)
    val tuples = value.collect()
    tuples(0)._2
  }
}
