package org.xi.maple.datacalc.sink

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.datacalc.api.MapleSink
import org.xi.maple.datacalc.util.VariableUtils

import java.sql.{Connection, DriverManager, PreparedStatement}
import scala.collection.JavaConverters._

class JdbcSink extends MapleSink[JdbcSinkConfig] {
  override def prepare(spark: SparkSession, variables: java.util.Map[String, String]): Unit = {
    config.setPreQueries(config.getPreQueries.asScala.map(query => VariableUtils.replaceVariables(query, variables)).asJava)
  }

  override def output(spark: SparkSession, ds: Dataset[Row]): Unit = {
    val targetTable = config.getTargetDatabase + "." + config.getTargetTable
    var options = Map(
      "url" -> config.getUrl,
      "driver" -> config.getDriver,
      "user" -> config.getUser,
      "password" -> config.getPassword,
      "dbtable" -> targetTable,
      "connectionCollation" -> "utf8mb4_unicode_ci"
    )

    if (config.getOptions != null && !config.getOptions.isEmpty) {
      options = config.getOptions.asScala.toMap ++ options
    }

    options = options ++ Map(
      "isolationLevel" -> options.getOrElse("isolationLevel", "NONE"),
      "batchsize" -> options.getOrElse("batchsize", "5000")
    )

    if (config.getPreQueries != null && !config.getPreQueries.isEmpty) {
      spark.sql("select 1").repartition(1).foreachPartition((_: Iterator[Row]) => {
        val jdbcOptions = new JDBCOptions(options)
        var conn: Connection = null
        try {
          conn = DriverManager.getConnection(config.getUrl, config.getUser, config.getPassword)
          config.getPreQueries.asScala.foreach(query => {
            logger.info(s"Execute pre query: $query")
            execute(conn, jdbcOptions, query)
          })
        } catch {
          case e: Exception => logger.error("Execute preQueries failed. ", e)
        } finally {
          if (conn != null) {
            conn.close()
          }
        }
      })
    }

    val writer = ds.write.format("jdbc")
    if (StringUtils.isNotBlank(config.getSaveMode)) {
      writer.mode(config.getSaveMode)
    }
    logger.info(s"Save data to jdbc url: ${config.getUrl}, driver: ${config.getDriver}, username: ${config.getUser}, table: $targetTable")
    writer.options(options).save()
  }

  private def execute(conn: Connection, jdbcOptions: JDBCOptions, query: String): Unit = {
    logger.info("Execute query: {}", query)
    var statement: PreparedStatement = null
    try {
      statement = conn.prepareStatement(query)
      statement.setQueryTimeout(jdbcOptions.queryTimeout)
      val rows = statement.executeUpdate()
      logger.info("{} rows affected", rows)
    } catch {
      case e: Exception => logger.error("Execute query failed. ", e)
    } finally {
      if (statement != null) {
        statement.close()
      }
    }
  }
}
