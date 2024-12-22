package org.xi.maple.datacalc.spark.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public abstract class MapleData implements Serializable {

    protected Map<String, String> variables = Collections.emptyMap();

    public void setVariables(Map<String, String> variables) {
        this.variables = Optional.ofNullable(variables).orElse(this.variables);
    }
}
