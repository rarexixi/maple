package org.apache.spark.ui

import org.apache.spark.SparkException
import org.apache.spark.sql.SparkSession
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization
import org.xi.maple.spark3.MapleApp
import org.xi.maple.spark3.common.Logging
import org.xi.maple.spark3.model.ExecuteRequest

import java.util.stream.Collectors
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

object ExtendedApiServer extends Logging {

  private val createServletHandler = Class.forName("org.apache.spark.ui.JettyUtils")
    .getMethod("createServletHandler", classOf[String], classOf[HttpServlet], classOf[String])

  private val attachHandler = Class.forName("org.apache.spark.ui.SparkUI")
    .getMethod("attachHandler", classOf[org.sparkproject.jetty.servlet.ServletContextHandler])

  def registerApis(spark: SparkSession): Unit = {
    // 开发时可以通过反射设定
    registerExecuteApi(spark)
    registerShutdownApi(spark)
  }

  // 开发时可以通过反射设定
  private def createServletHandler(spark: SparkSession, path: String, servlet: HttpServlet, basePath: String): Unit = {
    val ui: SparkUI = spark.sparkContext.ui.getOrElse {
      throw new SparkException("Parent SparkUI to attach this tab to not found!")
    }
    val executeHandler = createServletHandler.invoke(null, path, servlet, basePath)
    attachHandler.invoke(ui, executeHandler)
  }

  // 打包时可以直接调用
  /*private def createServletHandler(spark: SparkSession, path: String, servlet: HttpServlet, basePath: String): Unit = {
    val ui: SparkUI = spark.sparkContext.ui.getOrElse {
      throw new SparkException("Parent SparkUI to attach this tab to not found!")
    }
    val handler = JettyUtils.createServletHandler(path, servlet, basePath)
    ui.attachHandler(handler)
  }*/

  private def registerExecuteApi(spark: SparkSession): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info("==============================================================")
        val body = req.getReader.lines().collect(Collectors.joining("\n"))

        implicit val formats: DefaultFormats.type = DefaultFormats

        val sqlRequest = Serialization.read[ExecuteRequest](body)
        if ("json".equals(sqlRequest.resultType)) {
          val result = MapleApp.execJsonResult(sqlRequest.jobId, sqlRequest.code)
          val value: String = Serialization.write[Array[Map[String, Any]]](result)
          resp.getWriter.write(value)
        } else {
          val result = MapleApp.execSchematicResult(sqlRequest.jobId, sqlRequest.code)
          val value: String = Serialization.write[Map[String, Any]](result)
          resp.getWriter.write(value)
        }

        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    createServletHandler(spark, "/execute/sql", servlet, "")
  }

  private def registerShutdownApi(spark: SparkSession): Unit = {
    val servlet = new HttpServlet {
      override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        logger.info("==============================================================")
        MapleApp.close()
        resp.getWriter.write("success")
        resp.setStatus(HttpServletResponse.SC_OK)
      }
    }
    createServletHandler(spark, "/shutdown", servlet, "")
  }
}