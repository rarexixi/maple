package org.xi.maple.datacalc.flink.model;

import lombok.Data;

import java.util.Map;

@Data
public class TableDefinition {
    String catalog;
    String database;
    String name;
    PrimaryKeyDefinition primaryKeys;
    String comment;

    WatermarkDefinition watermark;
    Map<String, String> options;
}
