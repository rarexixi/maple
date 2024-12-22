package org.xi.maple.datacalc.spark.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MaplePluginConfig implements Serializable {

    protected boolean terminate = false;
}
