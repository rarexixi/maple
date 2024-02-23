package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public class FlinkPhysicalColumn extends FlinkColumn {

    String type;
    String comment;
}
