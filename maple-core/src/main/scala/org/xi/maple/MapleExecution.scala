package org.xi.maple

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.slf4j.{Logger, LoggerFactory}
import org.xi.maple.api._
import org.xi.maple.exception.ConfigRuntimeException
import org.xi.maple.model._
import org.xi.maple.util.{JsonUtils, PluginUtil}

import javax.validation.{Validation, Validator}
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.control.Breaks

object MapleExecution {

  private val log: Logger = LoggerFactory.getLogger(MapleExecution.getClass)

  private def getSourceAndCheck[SR <: SourceConfig](dc: MapleDataConfig,
                                                    gv: java.util.Map[String, String],
                                                    cr: CheckResult,
                                                    consumer: MapleSource[SR] => Unit): (SR, () => Unit) = {
    val plugin = PluginUtil.createSource[SR](dc.getName, dc.getConfig)
    plugin.getConfig.setVariables(mergeMap(plugin.getConfig.getVariables, gv))
    cr.checkResultTable(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  private def getTransformAndCheck[TR <: TransformConfig](dc: MapleDataConfig,
                                                          gv: java.util.Map[String, String],
                                                          cr: CheckResult,
                                                          consumer: MapleTransform[TR] => Unit): (TR, () => Unit) = {
    val plugin = PluginUtil.createTransform[TR](dc.getName, dc.getConfig)
    plugin.getConfig.setVariables(mergeMap(plugin.getConfig.getVariables, gv))
    cr.checkResultTable(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  private def getSinkAndCheck[SK <: SinkConfig](dc: MapleDataConfig,
                                                gv: java.util.Map[String, String],
                                                cr: CheckResult,
                                                consumer: MapleSink[SK] => Unit): (SK, () => Unit) = {
    val plugin = PluginUtil.createSink[SK](dc.getName, dc.getConfig)
    plugin.getConfig.setVariables(mergeMap(plugin.getConfig.getVariables, gv))
    cr.checkPluginConfig(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  def executeGroup[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig](spark: SparkSession,
                                                                                mapleData: MapleGroupData,
                                                                                terminateCondition: MaplePluginConfig => Boolean = null,
                                                                                dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    val cr = new CheckResult()
    val gv = mapleData.getVariables
    val sources = mapleData.getSources.map { dc =>
      getSourceAndCheck(dc, gv, cr, (plugin: MapleSource[SR]) => sourceProcess(spark, plugin, dsConsumer))
    }
    val transformations = mapleData.getTransformations.map { dc =>
      getTransformAndCheck(dc, gv, cr, (plugin: MapleTransform[TR]) => transformProcess(spark, plugin, dsConsumer))
    }
    val sinks = mapleData.getSinks.map { dc =>
      getSinkAndCheck(dc, gv, cr, (plugin: MapleSink[SK]) => sinkProcess(spark, plugin, dsConsumer))
    }
    cr.check()

    var isTermination: Boolean = false
    val terminable = terminateCondition != null

    def consumePlugin[T <: MaplePluginConfig](plugins: Array[(T, () => Unit)]): Unit =
      if (plugins != null && plugins.isEmpty && !isTermination) {
        val loop = new Breaks
        loop.breakable {
          plugins.foreach { case (config, process) =>
            process()
            isTermination = terminable && terminateCondition(config)
            if (isTermination) {
              loop.break()
            }
          }
        }
        plugins.foreach { case (_, process) => process() }
      }

    consumePlugin(sources)
    consumePlugin(transformations)
    consumePlugin(sinks)

    MapleTempData.clean(spark.sqlContext)
  }

  def executeArray[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig](spark: SparkSession,
                                                                                mapleData: MapleArrayData,
                                                                                terminateCondition: MaplePluginConfig => Boolean = null,
                                                                                dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    if (mapleData.getPlugins == null || mapleData.getPlugins.isEmpty) {
      throw new ConfigRuntimeException("plugins is empty")
    }

    val cr = new CheckResult()
    val gv = mapleData.getVariables
    val plugins = mapleData.getPlugins.map { dc =>
      dc.getType match {
        case "source" =>
          getSourceAndCheck(dc, gv, cr, (plugin: MapleSource[SR]) => sourceProcess(spark, plugin, dsConsumer))
        case "transformation" =>
          getTransformAndCheck(dc, gv, cr, (plugin: MapleTransform[TR]) => transformProcess(spark, plugin, dsConsumer))
        case "sink" =>
          getSinkAndCheck(dc, gv, cr, (plugin: MapleSink[SK]) => sinkProcess(spark, plugin, dsConsumer))
        case t: String =>
          throw new ConfigRuntimeException(s"[$t] is not a valid type")
      }
    }
    cr.check()

    val terminable = terminateCondition != null
    val loop = new Breaks
    loop.breakable {
      plugins.foreach { case (config, process) =>
        process()
        if (terminable && terminateCondition(config)) {
          loop.break()
        }
      }
    }

    MapleTempData.clean(spark.sqlContext)
  }

  private def mergeMap(map1: java.util.Map[String, String], map2: java.util.Map[String, String]): java.util.Map[String, String] = {
    val map = if (map1 != null) map1 else new java.util.HashMap[String, String]()
    if (map2 != null && !map2.isEmpty) {
      map2.entrySet().forEach(entry => {
        map.putIfAbsent(entry.getKey, entry.getValue)
      })
    }
    map
  }

  private def sourceProcess[T <: SourceConfig](spark: SparkSession, source: MapleSource[T], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    source.prepare(spark)
    val ds: Dataset[Row] = source.getData(spark)
    if (dfConsumer != null) {
      dfConsumer(source.getConfig, ds)
    }
    tempSaveResultTable(ds, source.getConfig)
  }

  private def transformProcess[T <: TransformConfig](spark: SparkSession, transform: MapleTransform[T], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    transform.prepare(spark)
    val fromDs: Dataset[Row] = if (StringUtils.isNotBlank(transform.getConfig.getSourceTable)) {
      spark.read.table(transform.getConfig.getSourceTable)
    } else {
      null
    }
    val ds: Dataset[Row] = transform.process(spark, fromDs)
    if (dfConsumer != null) {
      dfConsumer(transform.getConfig, ds)
    }
    tempSaveResultTable(ds, transform.getConfig)
  }

  private def sinkProcess[T <: SinkConfig](spark: SparkSession, sink: MapleSink[T], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    sink.prepare(spark)
    val fromDs: Dataset[Row] = if (StringUtils.isBlank(sink.getConfig.getSourceQuery)) {
      spark.read.table(sink.getConfig.getSourceTable)
    } else {
      spark.sql(sink.getConfig.getSourceQuery)
    }
    sink.output(spark, fromDs)
  }

  private def tempSaveResultTable(ds: Dataset[Row], resultTableConfig: ResultTableConfig): Unit = {
    if (ds == null) return

    ds.createOrReplaceTempView(resultTableConfig.getResultTable)
    MapleTempData.putResultTable(resultTableConfig.getResultTable)
    if (resultTableConfig.getPersist) {
      ds.persist(StorageLevel.fromString(resultTableConfig.getStorageLevel))
      MapleTempData.putPersistDataSet(ds)
    }
  }

  private class CheckResult {

    private var success: Boolean = true
    private val set: mutable.Set[String] = mutable.Set()

    val validator: Validator = Validation.buildDefaultValidatorFactory().getValidator

    def checkResultTable[T <: MaplePluginConfig with ResultTableConfig](config: T): Unit = {
      checkPluginConfig(config)
      if (set.contains(config.getResultTable)) {
        log.error(s"Result table [${config.getResultTable}] cannot be duplicate")
        success = false
      } else {
        set.add(config.getResultTable)
      }
    }

    def checkPluginConfig[T](config: MaplePluginConfig): Unit = {
      val violations = validator.validate(config)
      if (!violations.isEmpty) {
        success = false
        log.error(s"Configuration check error, ${JsonUtils.toJsonString(config)}")
        for (violation <- violations.asScala) {
          if (violation.getMessageTemplate.startsWith("{") && violation.getMessageTemplate.endsWith("}")) {
            log.error(s"[${violation.getPropertyPath}] ${violation.getMessage}")
          } else {
            log.error(violation.getMessage)
          }
        }
      }
    }

    def check(): Unit = {
      if (!success) {
        throw new ConfigRuntimeException("Config data valid failed")
      }
    }
  }
}
