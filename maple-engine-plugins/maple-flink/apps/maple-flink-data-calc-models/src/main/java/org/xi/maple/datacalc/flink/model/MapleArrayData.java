package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public class MapleArrayData extends MapleData implements Serializable {

    protected Map<String, String> variables = Collections.emptyMap();

    private MapleDataConfig[] plugins = new MapleDataConfig[0];

    public void setVariables(Map<String, String> variables) {
        this.variables = Optional.ofNullable(variables).orElse(this.variables);
    }

    public void setPlugins(MapleDataConfig[] plugins) {
        this.plugins = Optional.ofNullable(plugins).orElse(this.plugins);
    }

    public static MapleArrayData getData(String data) {
        return JsonUtils.parseObject(data, MapleArrayData.class, new MapleArrayData());
    }
}
