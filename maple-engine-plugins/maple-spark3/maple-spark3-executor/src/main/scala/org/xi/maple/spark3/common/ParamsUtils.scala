package org.xi.maple.spark3.common

object ParamsUtils {

  // java -Djob-id=xxx ...
  // spark-submit --conf spark.extraListeners=org.xi.maple.spark3.listener.OnceAppListener
  //              --conf spark.driver.extraJavaOptions="-Djob-id=xxx -Dupdate-engine-url=xxx -Dupdate-job-url=xxx -Dexecute-once=xxx"

  def engineId: Int = {
    sys.props.getOrElse("engine-id", "").toInt
  }

  def jobId: Option[String] = {
    sys.props.get("job-id")
  }

  def updateEngineUrl: String = {
    sys.props("update-engine-url")
  }

  def updateJobUrl: String = {
    sys.props("update-job-url")
  }

  def isOnce: Boolean = {
    sys.props.getOrElse("execute-once", "false").toBoolean
  }
}
