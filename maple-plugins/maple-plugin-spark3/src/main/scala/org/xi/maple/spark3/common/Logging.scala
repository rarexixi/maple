package org.xi.maple.spark3.common

import org.slf4j.LoggerFactory

trait Logging {

  protected lazy implicit val logger = LoggerFactory.getLogger(getClass)
}