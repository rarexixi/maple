package org.xi.maple.datacalc.spark.api

import org.xi.maple.datacalc.spark.model.SourceConfig

trait MapleSource[T <: SourceConfig] extends MapleResultTablePlugin[T] with Logging {
}
