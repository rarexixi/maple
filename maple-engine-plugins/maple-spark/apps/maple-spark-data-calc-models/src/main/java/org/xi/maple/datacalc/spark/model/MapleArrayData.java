package org.xi.maple.datacalc.spark.model;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
public class MapleArrayData extends MapleData implements Serializable {

    private MapleDataConfig[] plugins = new MapleDataConfig[0];

    public void setPlugins(MapleDataConfig[] plugins) {
        this.plugins = Optional.ofNullable(plugins).orElse(this.plugins);
    }

    public static MapleArrayData getData(String data) {
        return JsonUtils.parseObject(data, MapleArrayData.class, new MapleArrayData());
    }
}
