package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.JsonUtils;
import org.xi.maple.datacalc.util.VariableUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class MapleArrayData extends MaplePluginConfig implements Serializable {

    protected Map<String, String> variables = Collections.emptyMap();

    private MapleDataConfig[] plugins = new MapleDataConfig[0];

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = VariableUtils.getNotNullValue(variables, this.variables);
    }

    public MapleDataConfig[] getPlugins() {
        return plugins;
    }

    public void setPlugins(MapleDataConfig[] plugins) {
        this.plugins = VariableUtils.getNotNullValue(plugins, this.plugins);
    }

    public static MapleArrayData getData(String data) {
        return JsonUtils.parseObject(data, MapleArrayData.class, new MapleArrayData());
    }
}
