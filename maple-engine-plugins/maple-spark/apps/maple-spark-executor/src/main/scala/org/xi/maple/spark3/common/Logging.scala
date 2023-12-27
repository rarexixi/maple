package org.xi.maple.spark3.common

import org.slf4j.{Logger, LoggerFactory}

trait Logging {

  protected lazy implicit val logger: Logger = LoggerFactory.getLogger(getClass)
}