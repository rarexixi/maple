package org.xi.maple.datacalc.spark

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.xi.maple.common.util.{JsonUtils, VariableUtils}
import org.xi.maple.datacalc.spark.api.Logging
import org.xi.maple.datacalc.spark.exception.ConfigRuntimeException
import org.xi.maple.datacalc.spark.model._
import org.xi.maple.datacalc.spark.util.PluginUtil

import javax.validation.{Validation, Validator}
import scala.collection.JavaConverters._
import scala.collection.mutable

class MapleExecution[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig, T <: MapleData]
(val spark: SparkSession, mapleData: T, val dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit) extends Logging {

  private val gv: java.util.Map[String, String] = new java.util.HashMap[String, String]()
  for ((k, v) <- mapleData.getVariables.asScala) {
    gv.put(k, VariableUtils.replaceVariables(v, mapleData.getVariables))
  }

  private val VALIDATOR: Validator = Validation.buildDefaultValidatorFactory().getValidator

  private val RESULT_TABLE_SET: mutable.Set[String] = mutable.Set()

  def execute(): Unit = {
    mapleData match {
      case groupData: MapleGroupData => executeGroup(groupData)
      case arrayData: MapleArrayData => executeArray(arrayData)
      case _ => throw new ConfigRuntimeException(s"MapleData type [${mapleData.getClass}] is not supported")
    }
  }

  private def executeGroup(mapleData: MapleGroupData): Unit = {
    val sources = mapleData.getSources.map { dc => getExecution("source", dc) }
    val transformations = mapleData.getTransformations.map { dc => getExecution("transformation", dc) }
    val sinks = mapleData.getSinks.map { dc => getExecution("sink", dc) }
    val executions = sources ++ transformations ++ sinks
    executePlugins(executions)
    executions.foreach({ case (_, _, cleanAction) => cleanAction() })
  }

  private def executeArray(mapleData: MapleArrayData): Unit = {
    if (mapleData.getPlugins == null || mapleData.getPlugins.isEmpty) {
      throw new ConfigRuntimeException("plugins is empty")
    }
    val executions = mapleData.getPlugins.map { dc => getExecution(dc.getType, dc) }
    executePlugins(executions)
    executions.foreach({ case (_, _, cleanAction) => cleanAction() })
  }

  private def getExecution(dcType: String, dc: MapleDataConfig): (MaplePluginConfig, () => Unit, () => Unit) = {
    dcType match {
      case "source" =>
        val plugin = PluginUtil.createSource[SR](dc.getName, dc.getConfig, spark, gv)
        (plugin.getConfig, () => plugin.execute(), () => plugin.clean())
      case "transformation" =>
        val plugin = PluginUtil.createTransform[TR](dc.getName, dc.getConfig, spark, gv)
        (plugin.getConfig, () => plugin.execute(), () => plugin.clean())
      case "sink" =>
        val plugin = PluginUtil.createSink[SK](dc.getName, dc.getConfig, spark, gv)
        (plugin.getConfig, () => plugin.execute(), () => plugin.clean())
      case t: String =>
        throw new ConfigRuntimeException(s"[$t] is not a valid type")
    }
  }

  private def executePlugins(plugins: Array[(MaplePluginConfig, () => Unit, () => Unit)]): Unit = {
    for ((config, _, _) <- plugins) {
      if (!checkPluginConfig(config)) {
        throw new ConfigRuntimeException("Config data valid failed")
      }
    }
    for ((config, process, _) <- plugins) {
      process()
      if (config.isTerminate) {
        return
      }
    }
  }

  private def checkPluginConfig(config: MaplePluginConfig): Boolean = {
    var success = true
    val violations = VALIDATOR.validate(config)
    if (!violations.isEmpty) {
      success = false
      logger.error(s"Configuration check error, ${JsonUtils.toJsonString(config)}")
      for (violation <- violations.asScala) {
        if (violation.getMessageTemplate.startsWith("{") && violation.getMessageTemplate.endsWith("}")) {
          logger.error(s"[${violation.getPropertyPath}] ${violation.getMessage}")
        } else {
          logger.error(violation.getMessage)
        }
      }
    }
    config match {
      case c: ResultTableConfig =>
        if (RESULT_TABLE_SET.contains(c.getResultTable)) {
          logger.error(s"Result table [${c.getResultTable}] cannot be duplicate")
          success = false
        } else {
          RESULT_TABLE_SET.add(c.getResultTable)
        }
      case _ =>
    }
    success
  }

}
