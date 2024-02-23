package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public  class FlinkComputedColumn extends FlinkColumn {

    String expression;
    String comment;
}
