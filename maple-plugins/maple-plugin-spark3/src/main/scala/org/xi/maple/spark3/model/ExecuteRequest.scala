package org.xi.maple.spark3.model

case class ExecuteRequest(jobId: Int, code: String, runType: String, resultType: String = "json")