package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

import java.util.Map;

@Data
public class ColumnDefinition {
    String columnType;
    Map<String, ?> definition;
}
