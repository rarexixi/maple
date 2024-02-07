package org.xi.maple.datacalc.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class MapleData implements Serializable {

    protected Map<String, String> variables = Collections.emptyMap();

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = Optional.ofNullable(variables).orElse(this.variables);
    }
}
