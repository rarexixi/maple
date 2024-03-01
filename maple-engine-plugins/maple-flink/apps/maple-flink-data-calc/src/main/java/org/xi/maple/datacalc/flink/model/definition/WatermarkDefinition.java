package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

@Data
public class WatermarkDefinition {

    String columnName;
    String expression;
}
