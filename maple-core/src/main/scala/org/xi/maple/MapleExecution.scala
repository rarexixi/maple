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
import scala.collection.JavaConversions._
import scala.collection.mutable

object MapleExecution {

  private val log: Logger = LoggerFactory.getLogger(MapleExecution.getClass)

  def getPlugins[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig](mapleData: MapleGroupData): (Array[MapleSource[SR]], Array[MapleTransform[TR]], Array[MapleSink[SK]]) = {
    val sources = mapleData.getSources.map(source => PluginUtil.createSource[SR](source.getName, source.getConfig))
    val transformations = mapleData.getTransformations.map(sink => PluginUtil.createTransform[TR](sink.getName, sink.getConfig))
    val sinks = mapleData.getSinks.map(sink => PluginUtil.createSink[SK](sink.getName, sink.getConfig))

    val checkResult = new CheckResult()
    sources.foreach(source => {
      source.getConfig.setVariables(mapleData.getVariables)
      checkResult.checkResultTable(source)
    })
    transformations.foreach(transformation => {
      transformation.getConfig.setVariables(mapleData.getVariables)
      checkResult.checkResultTable(transformation)
    })
    sinks.foreach(sink => {
      sink.getConfig.setVariables(mapleData.getVariables)
      checkResult.checkPluginConfig(sink)
    })
    checkResult.check()

    (sources, transformations, sinks)
  }

  def execute[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig](spark: SparkSession, sources: Array[MapleSource[SR]], transformations: Array[MapleTransform[TR]], sinks: Array[MapleSink[SK]]): Unit = {
    if (sources != null && !sources.isEmpty) sources.foreach(source => sourceProcess(spark, source))
    if (transformations != null && !transformations.isEmpty) transformations.foreach(transformation => transformProcess(spark, transformation))
    if (sinks != null && !sinks.isEmpty) sinks.foreach(sink => sinkProcess(spark, sink))

    MapleTempData.clean(spark.sqlContext)
  }

  def getPlugins[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig, T <: Object](mapleData: MapleArrayData): Array[Any] = {
    val checkResult = new CheckResult()
    val plugins = new Array[Any](mapleData.getPlugins.length)
    for (i <- mapleData.getPlugins.indices) {
      val config = mapleData.getPlugins()(i)
      config.getType match {
        case "source" =>
          val source = PluginUtil.createSource[SR](config.getName, config.getConfig)
          source.getConfig.setVariables(mapleData.getVariables)
          checkResult.checkResultTable(source)
          plugins(i) = source
        case "transformation" =>
          val transformation = PluginUtil.createTransform[TR](config.getName, config.getConfig)
          transformation.getConfig.setVariables(mapleData.getVariables)
          checkResult.checkResultTable(transformation)
          plugins(i) = transformation
        case "sink" =>
          val sink = PluginUtil.createSink[SK](config.getName, config.getConfig)
          sink.getConfig.setVariables(mapleData.getVariables)
          checkResult.checkPluginConfig(sink)
          plugins(i) = sink
        case t: String =>
          throw new ConfigRuntimeException(s"[$t] is not a valid type")
      }
    }
    checkResult.check()
    plugins
  }

  def execute[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig, T <: Object](spark: SparkSession, plugins: Array[Any]): Unit = {
    if (plugins == null || plugins.isEmpty) return
    plugins.foreach {
      case source: MapleSource[SR] => sourceProcess(spark, source)
      case transform: MapleTransform[TR] => transformProcess(spark, transform)
      case sink: MapleSink[SK] => sinkProcess(spark, sink)
      case _ =>
    }

    MapleTempData.clean(spark.sqlContext)
  }

  private def sourceProcess[T <: SourceConfig](spark: SparkSession, source: MapleSource[T]): Unit = {
    source.prepare(spark)
    val ds: Dataset[Row] = source.getData(spark)
    tempSaveResultTable(ds, source.getConfig)
  }

  private def transformProcess[T <: TransformConfig](spark: SparkSession, transform: MapleTransform[T]): Unit = {
    transform.prepare(spark)
    val fromDs: Dataset[Row] = if (StringUtils.isNotBlank(transform.getConfig.getSourceTable)) {
      spark.read.table(transform.getConfig.getSourceTable)
    } else {
      null
    }
    val ds: Dataset[Row] = transform.process(spark, fromDs)
    tempSaveResultTable(ds, transform.getConfig)
  }

  private def sinkProcess[T <: SinkConfig](spark: SparkSession, sink: MapleSink[T]): Unit = {
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

    def checkResultTable[T <: ResultTableConfig](plugin: MaplePlugin[T]): Unit = {
      checkPluginConfig(plugin)
      if (set.contains(plugin.getConfig.getResultTable)) {
        log.error(s"Result table [${plugin.getConfig.getResultTable}] cannot be duplicate")
        success = false
      } else {
        set.add(plugin.getConfig.getResultTable)
      }
    }

    def checkPluginConfig[T](plugin: MaplePlugin[T]): Unit = {
      val violations = validator.validate(plugin.getConfig)
      if (!violations.isEmpty) {
        success = false
        log.error(s"Configuration check error, ${JsonUtils.toJsonString(plugin.getConfig)}")
        for (violation <- violations) {
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
