package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

@Data
public class WatermarkDefinition {

    String columnName;
    Integer delaySeconds;

    public String getExpression() {
        return String.format("%s - INTERVAL '%d' SECOND", columnName, delaySeconds);
    }
}
