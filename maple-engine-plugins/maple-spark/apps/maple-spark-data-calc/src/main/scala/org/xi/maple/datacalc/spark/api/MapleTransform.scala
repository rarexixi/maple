package org.xi.maple.datacalc.spark.api

import org.xi.maple.datacalc.spark.model.TransformConfig

trait MapleTransform[T <: TransformConfig] extends MapleResultTablePlugin[T] with Logging {
}

