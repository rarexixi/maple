package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.VariableUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class MapleData implements Serializable {

    protected Map<String, String> variables = Collections.emptyMap();

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = VariableUtils.getNotNullValue(variables, this.variables);
    }
}
