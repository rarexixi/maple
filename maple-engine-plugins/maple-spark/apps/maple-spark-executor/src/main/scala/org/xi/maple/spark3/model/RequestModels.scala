package org.xi.maple.spark3.model

case class ExecuteRequest(jobId: Int, code: String, runType: String, resultType: String = "json")

case class AddUdfRequest(path: String, name: String, code: String, udfType: String = "scala")