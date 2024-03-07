package org.xi.maple.datacalc.flink.api;

import java.io.Serializable;

public interface TableInsert extends Serializable {
    String getInsertSql();
}
