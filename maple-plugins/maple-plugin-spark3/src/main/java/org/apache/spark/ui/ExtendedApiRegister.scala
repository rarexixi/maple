package org.apache.spark.ui

import org.apache.spark.SparkException
import org.apache.spark.sql.SparkSession
import org.xi.maple.spark3.common.Logging

import javax.servlet.http.HttpServlet

object ExtendedApiRegister extends Logging {

  private val createServletHandler = Class.forName("org.apache.spark.ui.JettyUtils")
    .getMethod("createServletHandler", classOf[String], classOf[HttpServlet], classOf[String])

  private val attachHandler = Class.forName("org.apache.spark.ui.SparkUI")
    .getMethod("attachHandler", classOf[org.sparkproject.jetty.servlet.ServletContextHandler])

  // 开发时可以通过反射设定
  def createServletHandler(spark: SparkSession, path: String, servlet: HttpServlet, basePath: String = ""): Unit = {
    val ui: SparkUI = spark.sparkContext.ui.getOrElse {
      throw new SparkException("Parent SparkUI to attach this tab to not found!")
    }
    val executeHandler = createServletHandler.invoke(null, path, servlet, basePath)
    attachHandler.invoke(ui, executeHandler)
  }

  // 打包时可以直接调用
  /*private def createServletHandler(spark: SparkSession, path: String, servlet: HttpServlet, basePath: String = ""): Unit = {
    val ui: SparkUI = spark.sparkContext.ui.getOrElse {
      throw new SparkException("Parent SparkUI to attach this tab to not found!")
    }
    val handler = JettyUtils.createServletHandler(path, servlet, basePath)
    ui.attachHandler(handler)
  }*/

}