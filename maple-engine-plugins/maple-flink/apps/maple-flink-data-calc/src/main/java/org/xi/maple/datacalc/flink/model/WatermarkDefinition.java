package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public class WatermarkDefinition {

    String columnName;
    String strategyExpression;
}
