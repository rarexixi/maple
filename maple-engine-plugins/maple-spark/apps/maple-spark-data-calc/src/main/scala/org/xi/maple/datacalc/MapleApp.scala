package org.xi.maple.datacalc

import org.apache.commons.lang3.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}
import org.xi.maple.datacalc.exception.ConfigRuntimeException
import org.xi.maple.datacalc.model.{MapleArrayData, MapleData, MapleGroupData}

import java.io.IOException
import java.nio.file.{Files, Paths}

object MapleApp {

  private val log: Logger = LoggerFactory.getLogger(MapleApp.getClass)

  def main(args: Array[String]): Unit = {
    // val argsMap = ArgsParser.getParams(args)
    // val config: String = if (argsMap.contains("data")) argsMap("data") else getContent(argsMap("file"))
    val config: String = getContent("/home/linkis/Projects/opensource/maple/examples/data-group.json")
    val execType = "group"

    val data: MapleData = if ("group" == execType) MapleGroupData.getData(config) else MapleArrayData.getData(config)
    val spark = SparkSession.builder.config(createSparkConf()).getOrCreate()
    try {
      val execution = new MapleExecution(spark, data.getVariables, null)
      execution.execute(data)
    } finally {
      spark.close()
    }
  }

  @throws[IOException]
  def getContent(path: String): String = {
    if (StringUtils.isBlank(path)) throw new ConfigRuntimeException("Path cannot be empty")
    val filePath = Paths.get(path)
    val bytes = Files.readAllBytes(filePath)
    new String(bytes)
  }

  private def createSparkConf() = {
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
    sparkConf.set("spark.sql.warehouse.dir", "/apps/hive/warehouse")
    //    sparkConf.set("hive.metastore.uris", "thrift://hive-meta-01:9083,thrift://hive-meta-02:9083")
    //    sparkConf.set("hive.exec.dynamic.partition", "true")
    //    sparkConf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    System.setProperty("HADOOP_USER_NAME", "azkaban")
    sparkConf
  }
}
