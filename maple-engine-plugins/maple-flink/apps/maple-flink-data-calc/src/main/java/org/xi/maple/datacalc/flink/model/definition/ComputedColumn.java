package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

@Data
public class ComputedColumn extends BaseColumn {

    String expression;
}
