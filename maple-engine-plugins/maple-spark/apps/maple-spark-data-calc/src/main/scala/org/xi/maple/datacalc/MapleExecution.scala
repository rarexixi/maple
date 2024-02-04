package org.xi.maple.datacalc

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.slf4j.{Logger, LoggerFactory}
import org.xi.maple.datacalc.api.{MapleSink, MapleSource, MapleTransform}
import org.xi.maple.datacalc.exception.ConfigRuntimeException
import org.xi.maple.datacalc.model._
import org.xi.maple.datacalc.util.{JsonUtils, PluginUtil}

import javax.validation.{Validation, Validator}
import scala.collection.JavaConverters._
import scala.collection.mutable

class MapleExecution[SR <: SourceConfig, TR <: TransformConfig, SK <: SinkConfig]
(val spark: SparkSession, val gv: java.util.Map[String, String], val dsConsumer: (MaplePluginConfig, Dataset[Row]) => Unit) {

  private val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  private val VALIDATOR: Validator = Validation.buildDefaultValidatorFactory().getValidator

  private val RESULT_TABLE_SET: mutable.Set[String] = mutable.Set()
  private val RESULT_TABLES: mutable.Set[String] = mutable.Set[String]()
  private val PERSIST_DATASETS: mutable.Set[Dataset[Row]] = mutable.Set[Dataset[Row]]()

  def execute[T <: MapleData](mapleData: T): Unit = {
    mapleData match {
      case groupData: MapleGroupData => executeGroup(groupData)
      case arrayData: MapleArrayData => executeArray(arrayData)
      case _ => throw new ConfigRuntimeException("MapleData type is not supported")
    }
  }

  private def executeGroup(mapleData: MapleGroupData): Unit = {
    val sources = mapleData.getSources.map { dc => getPluginAndCheck("source", dc) }
    val transformations = mapleData.getTransformations.map { dc => getPluginAndCheck("transformation", dc) }
    val sinks = mapleData.getSinks.map { dc => getPluginAndCheck("sink", dc) }
    executePlugins(sources ++ transformations ++ sinks)
    clean()
  }

  private def executeArray(mapleData: MapleArrayData): Unit = {
    if (mapleData.getPlugins == null || mapleData.getPlugins.isEmpty) {
      throw new ConfigRuntimeException("plugins is empty")
    }
    val plugins = mapleData.getPlugins.map { dc => getPluginAndCheck(dc.getType, dc) }
    executePlugins(plugins)
    clean()
  }

  private def getPluginAndCheck(dcType: String, dc: MapleDataConfig): (MaplePluginConfig, () => Unit) = {
    dcType match {
      case "source" =>
        val plugin = PluginUtil.createSource[SR](dc.getName, dc.getConfig)
        (plugin.getConfig, () => sourceProcess(plugin))
      case "transformation" =>
        val plugin = PluginUtil.createTransform[TR](dc.getName, dc.getConfig)
        (plugin.getConfig, () => transformProcess(plugin))
      case "sink" =>
        val plugin = PluginUtil.createSink[SK](dc.getName, dc.getConfig)
        (plugin.getConfig, () => sinkProcess(plugin))
      case t: String =>
        throw new ConfigRuntimeException(s"[$t] is not a valid type")
    }
  }

  private def executePlugins(plugins: Array[(MaplePluginConfig, () => Unit)]): Unit = {
    for ((config, _) <- plugins) {
      if (!checkPluginConfig(config)) {
        throw new ConfigRuntimeException("Config data valid failed")
      }
    }
    for ((config, process) <- plugins) {
      process()
      if (config.isTerminate) {
        return
      }
    }
  }

  private def sourceProcess(source: MapleSource[SR]): Unit = {
    source.prepare(spark, gv)
    val ds: Dataset[Row] = source.getData(spark)
    if (dsConsumer != null) {
      dsConsumer(source.getConfig, ds)
    }
    tempSaveResultTable(ds, source.getConfig)
  }

  private def transformProcess(transform: MapleTransform[TR]): Unit = {
    transform.prepare(spark, gv)
    val fromDs: Dataset[Row] = if (StringUtils.isNotBlank(transform.getConfig.getSourceTable)) {
      spark.read.table(transform.getConfig.getSourceTable)
    } else {
      null
    }
    val ds: Dataset[Row] = transform.process(spark, fromDs)
    if (dsConsumer != null) {
      dsConsumer(transform.getConfig, ds)
    }
    tempSaveResultTable(ds, transform.getConfig)
  }

  private def sinkProcess(sink: MapleSink[SK]): Unit = {
    sink.prepare(spark, gv)
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
    RESULT_TABLES.add(resultTableConfig.getResultTable)
    if (resultTableConfig.getPersist) {
      ds.persist(StorageLevel.fromString(resultTableConfig.getStorageLevel))
      PERSIST_DATASETS.add(ds)
    }
  }

  private def checkPluginConfig(config: MaplePluginConfig): Boolean = {
    var success = true
    val violations = VALIDATOR.validate(config)
    if (!violations.isEmpty) {
      success = false
      LOG.error(s"Configuration check error, ${JsonUtils.toJsonString(config)}")
      for (violation <- violations.asScala) {
        if (violation.getMessageTemplate.startsWith("{") && violation.getMessageTemplate.endsWith("}")) {
          LOG.error(s"[${violation.getPropertyPath}] ${violation.getMessage}")
        } else {
          LOG.error(violation.getMessage)
        }
      }
    }
    config match {
      case c: ResultTableConfig =>
        if (RESULT_TABLE_SET.contains(c.getResultTable)) {
          LOG.error(s"Result table [${c.getResultTable}] cannot be duplicate")
          success = false
        } else {
          RESULT_TABLE_SET.add(c.getResultTable)
        }
      case _ =>
    }
    success
  }

  private def clean(): Unit = {
    RESULT_TABLES.foreach(resultTable => spark.sqlContext.dropTempTable(resultTable))
    RESULT_TABLES.clear()

    PERSIST_DATASETS.foreach(ds => ds.unpersist())
    PERSIST_DATASETS.clear()
  }
}
