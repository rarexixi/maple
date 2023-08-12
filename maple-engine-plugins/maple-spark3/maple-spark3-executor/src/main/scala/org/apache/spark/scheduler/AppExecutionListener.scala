package org.apache.spark.scheduler

import org.xi.maple.common.constant.EngineExecutionStatus
import org.xi.maple.common.util.JsonUtils
import org.xi.maple.engine.common.service.EngineExecutionUpdateService

import scala.collection.JavaConverters._

class AppExecutionListener extends SparkListener {

  var succeeded = true

  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {

    println("========================= onApplicationStart =========================")

    val map = Map("applicationId" -> applicationStart.appId.getOrElse("No_Application_Id"))
    val info = JsonUtils.toJsonString(map.asJava)

    EngineExecutionUpdateService.updateStatus(EngineExecutionStatus.RUNNING)
    EngineExecutionUpdateService.updateInfo(info)
  }


  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    val status = if (succeeded) EngineExecutionStatus.SUCCEED else EngineExecutionStatus.FAILED
    EngineExecutionUpdateService.updateStatus(status)
    println("========================== onApplicationEnd ==========================")
  }

  override def onStageSubmitted(stageSubmitted: SparkListenerStageSubmitted): Unit = {

  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    succeeded = "succeeded".equals(stageCompleted.stageInfo.getStatusString)
  }
}
