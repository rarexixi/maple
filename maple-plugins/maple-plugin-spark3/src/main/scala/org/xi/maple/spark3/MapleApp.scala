package org.xi.maple.spark3

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.ui.ExtendedApiRegister
import org.json4s.DefaultFormats
import org.json4s.jackson.{JsonMethods, Serialization}
import org.xi.maple.engine.common._
import org.xi.maple.spark3.common.{Logging, ParamsUtils}
import org.xi.maple.spark3.model.{AddUdfRequest, ExecuteRequest}

import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

object MapleApp extends Logging {

  private var spark: SparkSession = _
  private var sc: SparkContext = _
  private var latch: CountDownLatch = _
  private val engineUpdateService: EngineUpdateService = EngineUpdateService.getInstance(ParamsUtils.updateEngineUrl)
  private val jobUpdateService: JobUpdateService = JobUpdateService.getInstance(ParamsUtils.updateEngineUrl)

  def main(args: Array[String]): Unit = {

    val sparkConf = createSparkConf()
    spark = SparkSession.builder.config(sparkConf).getOrCreate()
    sc = spark.sparkContext

    val model = new EngineUpdateModel().setId(ParamsUtils.engineId).setAddress(sc.uiWebUrl.getOrElse(""))
    engineUpdateService.update(model)

    if (ParamsUtils.isOnce) {
      exec(jobId = 0, "")
    } else {
      registerApis()
      latch = new CountDownLatch(1)
      latch.await()
    }
    spark.close()
  }

  private def exec(jobId: Int, sql: String): String = {
    sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
    val df = spark.sql(sql)
    val path = """/tmp/maple/""" + UUID.randomUUID().toString
    val model = new JobUpdateModel().setId(jobId).setResult(path)
    jobUpdateService.update(model)
    df.write.mode("overwrite").parquet(path)
    sc.clearJobGroup()
    path
  }

  private def registerApis(): Unit = {
    registerSyncExecute()
    registerAsyncExecute()
    registerAddUDF()
    registerShutdown()
  }

  /**
   * 注册同步执行接口
   */
  private def registerSyncExecute(): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info("Start to execute sync")

        val executeRequest = getJsonResultFromDataFrame[ExecuteRequest](req)
        implicit val formats: DefaultFormats.type = DefaultFormats

        val result: String = if ("json".equals(executeRequest.resultType)) {
          val parseResult = (df: DataFrame) => df.collect().map(
            (row: Row) => JsonMethods.parse(row.json).extract[Map[String, Any]]
          )
          val data = getResultFromDataFrame(spark, executeRequest.jobId, executeRequest.code, parseResult)
          Serialization.write[Array[Map[String, Any]]](data)
        } else {
          val parseResult = (df: DataFrame) => Map(
            "schema" -> df.schema.fields.map(item => Map(
              "name" -> item.name,
              "type" -> item.dataType.typeName,
              "nullable" -> item.nullable
            )),
            "data" -> df.collect().map(row => row.toSeq)
          )
          val data = getResultFromDataFrame(spark, executeRequest.jobId, executeRequest.code, parseResult)
          Serialization.write[Map[String, Any]](data)
        }

        resp.getWriter.write(result)
        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    ExtendedApiRegister.createServletHandler(spark, "/execute/sql", servlet, "")
  }

  /**
   * 注册异步执行接口
   */
  private def registerAsyncExecute(): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info("Start to execute async")

        val executeRequest = getJsonResultFromDataFrame[ExecuteRequest](req)
        val path = exec(executeRequest.jobId, executeRequest.code)
        resp.getWriter.write(path)

        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    ExtendedApiRegister.createServletHandler(spark, "/execute/sql", servlet, "")
  }

  /**
   * 注册添加UDF接口
   */
  private def registerAddUDF(): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info("Start to add udf")

        val udfRequest = getJsonResultFromDataFrame[AddUdfRequest](req)
        sc.addJar(udfRequest.path)

        udfRequest.udfType match {
          case "scala" =>
            // todo 调用 scala 执行
            spark.udf.register(udfRequest.name, udfRequest.code)
          case "sql" =>
            spark.sql(udfRequest.code)
          case "jar" =>
          case _ =>
            logger.error(s"Unknown udf type ${udfRequest.udfType}")
        }

        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    ExtendedApiRegister.createServletHandler(spark, "/udf/add", servlet, "")
  }

  /**
   * 注册关闭接口
   */
  private def registerShutdown(): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        shutdown(req, resp)
      }

      override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        shutdown(req, resp)
      }

      private def shutdown(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info(s"Shutdown this engine ${sc.applicationId}")
        latch.countDown()
        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    ExtendedApiRegister.createServletHandler(spark, "/shutdown", servlet, "")
  }

  private def getJsonResultFromDataFrame[T](req: HttpServletRequest): T = {
    val body = req.getReader.lines().collect(Collectors.joining("\n"))
    implicit val formats: DefaultFormats.type = DefaultFormats
    Serialization.read[T](body)
  }

  private def getResultFromDataFrame[T](spark: SparkSession, jobId: Int, sql: String, parse: DataFrame => T): T = {
    sc.setJobGroup(groupId = jobId.toString, description = sql, interruptOnCancel = true)
    val df = spark.sql(sql)
    val result = parse(df)
    sc.clearJobGroup()
    result
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
