package org.xi.maple.datacalc.spark.api

import org.slf4j.{Logger, LoggerFactory}

trait Logging {
  val logger: Logger = LoggerFactory.getLogger(getClass)
}
