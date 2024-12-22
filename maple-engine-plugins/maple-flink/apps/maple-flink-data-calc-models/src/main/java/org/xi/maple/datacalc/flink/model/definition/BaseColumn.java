package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

@Data
public abstract class BaseColumn {
    String name;
    String comment;
}
