package org.xi.maple.datacalc.flink.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class MapleDataConfig implements Serializable {

    private String type;
    private String name;
    private Map<String, Object> config;
}