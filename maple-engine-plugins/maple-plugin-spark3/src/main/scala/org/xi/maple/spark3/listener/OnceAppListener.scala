package org.xi.maple.spark3.listener

import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd, SparkListenerApplicationStart}
import org.xi.maple.engine.common.{EngineUpdateModel, EngineUpdateService}
import org.xi.maple.spark3.common.ParamsUtils

class OnceAppListener extends SparkListener {
  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {

    println("========================= onApplicationStart =========================")

    val model = new EngineUpdateModel()
      .setId(ParamsUtils.engineId)
      .setApplicationId(applicationStart.appId.getOrElse("No_Application_Id"))
    EngineUpdateService.getInstance(ParamsUtils.updateEngineUrl).update(model)
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    println("========================== onApplicationEnd ==========================")
  }
}
