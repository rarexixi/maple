package org.xi.maple.datacalc.spark.model;

import java.io.Serializable;

public interface ResultTableConfig extends Serializable {
    String getResultTable();
    Boolean getPersist();
    String getStorageLevel();
}
