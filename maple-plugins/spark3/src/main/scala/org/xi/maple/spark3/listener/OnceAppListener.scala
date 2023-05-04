package org.xi.maple.spark3.listener

import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd, SparkListenerApplicationStart}

class OnceAppListener extends SparkListener {
  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {
    println("========================= onApplicationStart =========================")
    println(applicationStart.appId)
    println(applicationStart.appName)
    // java -Djob-id=xxx ...
    val jobId = sys.props.get("job-id")

    println("======================================================================")
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    println("========================== onApplicationEnd ==========================")
    val jobId = sys.props.get("job-id")
    println("======================================================================")
  }
}
