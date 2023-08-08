package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.JsonUtils;

import java.io.Serializable;

public class MapleArrayData extends MaplePluginConfig implements Serializable {

    private MapleDataConfig[] plugins = new MapleDataConfig[0];

    public MapleDataConfig[] getPlugins() {
        return plugins;
    }

    public void setPlugins(MapleDataConfig[] plugins) {
        this.plugins = plugins;
    }

    public static MapleArrayData getData(String data) {
        return JsonUtils.parseObject(data, MapleArrayData.class, new MapleArrayData());
    }
}
