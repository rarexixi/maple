package org.xi.maple.datacalc.flink.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MapleConfig implements Serializable {
    protected String type;
    protected boolean terminate = false;
}
