package org.xi.maple.datacalc

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.slf4j.{Logger, LoggerFactory}
import org.xi.maple.api.{MapleSink, MapleSource, MapleTransform}
import org.xi.maple.datacalc.exception.ConfigRuntimeException
import org.xi.maple.datacalc.model._
import org.xi.maple.datacalc.util.{JsonUtils, PluginUtil}

import javax.validation.{Validation, Validator}
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.control.Breaks

object MapleExecution {

  private val log: Logger = LoggerFactory.getLogger(MapleExecution.getClass)

  private def getSourceAndCheck[SR <: SourceConfig](dc: MapleDataConfig,
                                                    cr: CheckResult,
                                                    consumer: MapleSource[SR] => Unit): (SR, () => Unit) = {
    val plugin = PluginUtil.createSource[SR](dc.getName, dc.getConfig)
    cr.checkResultTable(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  private def getTransformAndCheck[TR <: TransformConfig](dc: MapleDataConfig,
                                                          cr: CheckResult,
                                                          consumer: MapleTransform[TR] => Unit): (TR, () => Unit) = {
    val plugin = PluginUtil.createTransform[TR](dc.getName, dc.getConfig)
    cr.checkResultTable(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  private def getSinkAndCheck[SK <: SinkConfig](dc: MapleDataConfig,
                                                cr: CheckResult,
                                                consumer: MapleSink[SK] => Unit): (SK, () => Unit) = {
    val plugin = PluginUtil.createSink[SK](dc.getName, dc.getConfig)
    cr.checkPluginConfig(plugin.getConfig)
    (plugin.getConfig, () => consumer(plugin))
  }

  def executeGroup[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig](spark: SparkSession,
                                                                                mapleData: MapleGroupData,
                                                                                dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    val cr = new CheckResult()
    val gv = mapleData.getVariables
    val sources = mapleData.getSources.map { dc =>
      getSourceAndCheck(dc, cr, (plugin: MapleSource[SR]) => sourceProcess(spark, plugin, gv, dsConsumer))
    }
    val transformations = mapleData.getTransformations.map { dc =>
      getTransformAndCheck(dc,  cr, (plugin: MapleTransform[TR]) => transformProcess(spark, plugin, gv, dsConsumer))
    }
    val sinks = mapleData.getSinks.map { dc =>
      getSinkAndCheck(dc,  cr, (plugin: MapleSink[SK]) => sinkProcess(spark, plugin, gv, dsConsumer))
    }
    cr.check()

    var isTerminate: Boolean = false
    def consumePlugin[T <: MaplePluginConfig](plugins: Array[(T, () => Unit)]): Unit =
      if (plugins != null && plugins.isEmpty && !isTerminate) {
        val loop = new Breaks
        loop.breakable {
          plugins.foreach { case (config, process) =>
            process()
            isTerminate = config.isTerminate
            if (isTerminate) {
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
                                                                                dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    if (mapleData.getPlugins == null || mapleData.getPlugins.isEmpty) {
      throw new ConfigRuntimeException("plugins is empty")
    }

    val cr = new CheckResult()
    val gv = mapleData.getVariables
    val plugins = mapleData.getPlugins.map { dc =>
      dc.getType match {
        case "source" =>
          getSourceAndCheck(dc, cr, (plugin: MapleSource[SR]) => sourceProcess(spark, plugin, gv, dsConsumer))
        case "transformation" =>
          getTransformAndCheck(dc, cr, (plugin: MapleTransform[TR]) => transformProcess(spark, plugin, gv, dsConsumer))
        case "sink" =>
          getSinkAndCheck(dc, cr, (plugin: MapleSink[SK]) => sinkProcess(spark, plugin, gv, dsConsumer))
        case t: String =>
          throw new ConfigRuntimeException(s"[$t] is not a valid type")
      }
    }
    cr.check()

    val loop = new Breaks
    loop.breakable {
      plugins.foreach { case (config, process) =>
        process()
        if (config.isTerminate) {
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

  private def sourceProcess[T <: SourceConfig](spark: SparkSession, source: MapleSource[T], gv: java.util.Map[String, String], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    source.replaceVariables(gv)
    source.prepare(spark)
    val ds: Dataset[Row] = source.getData(spark)
    if (dfConsumer != null) {
      dfConsumer(source.getConfig, ds)
    }
    tempSaveResultTable(ds, source.getConfig)
  }

  private def transformProcess[T <: TransformConfig](spark: SparkSession, transform: MapleTransform[T], gv: java.util.Map[String, String], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    transform.replaceVariables(gv)
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

  private def sinkProcess[T <: SinkConfig](spark: SparkSession, sink: MapleSink[T], gv: java.util.Map[String, String], dfConsumer: (MaplePluginConfig, Dataset[Row]) => Unit = null): Unit = {
    sink.replaceVariables(gv)
    sink.prepare(spark)
    val fromDs: Dataset[Row] = if (StringUtils.isBlank(sink.getConfig.getSourceQuery)) {
      spark.read.table(sink.getConfig.getSourceTable)
    } else {
      spark.sql(sink.getConfig.getSourceQuery)
    }
    val partitions = sink.getConfig.getNumPartitions
    if (partitions != null && partitions > 0) {
      sink.output(spark, fromDs.repartition(partitions))
    } else {
      sink.output(spark, fromDs)
    }
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
