package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public class PrimaryKeyDefinition {

    String name;
    String[] columns;
}
