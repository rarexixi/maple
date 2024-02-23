package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public class ColumnDefinition<T extends FlinkColumn> {
    String type;
    T definition;
}
