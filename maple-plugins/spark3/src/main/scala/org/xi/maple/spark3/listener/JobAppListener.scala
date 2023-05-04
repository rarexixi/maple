package org.xi.maple.spark3.listener

import org.apache.spark.scheduler._

class JobAppListener extends SparkListener {

  override def onStageSubmitted(stageSubmitted: SparkListenerStageSubmitted): Unit = {
    super.onStageSubmitted(stageSubmitted)
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    super.onStageCompleted(stageCompleted)
  }

  override def onTaskStart(taskStart: SparkListenerTaskStart): Unit = {
    super.onTaskStart(taskStart)
  }

  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    taskEnd.taskMetrics
    super.onTaskEnd(taskEnd)
  }

  override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
    println(s"========= onJobStart: jobId: ${jobStart.jobId}, jobGroupId: ${jobStart.properties.getProperty("spark.jobGroup.id")}")
  }

  override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = {
    println(s"========= onJobEnd: jobId: ${jobEnd.jobId}, jobResult: ${jobEnd.jobResult}")
  }
}
