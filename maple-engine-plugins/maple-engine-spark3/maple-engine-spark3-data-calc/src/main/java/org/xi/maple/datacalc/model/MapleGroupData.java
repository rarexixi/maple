package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.JsonUtils;

import java.io.Serializable;

public class MapleGroupData extends MaplePluginConfig implements Serializable {

    private MapleDataConfig[] sources = new MapleDataConfig[0];
    private MapleDataConfig[] transformations = new MapleDataConfig[0];
    private MapleDataConfig[] sinks = new MapleDataConfig[0];

    public MapleDataConfig[] getSources() {
        return sources;
    }

    public void setSources(MapleDataConfig[] sources) {
        if (sources != null) {
            this.sources = sources;
        }
    }

    public MapleDataConfig[] getTransformations() {
        return transformations;
    }

    public void setTransformations(MapleDataConfig[] transformations) {
        if (transformations != null) {
            this.transformations = transformations;
        }
    }

    public MapleDataConfig[] getSinks() {
        return sinks;
    }

    public void setSinks(MapleDataConfig[] sinks) {
        if (sinks != null) {
            this.sinks = sinks;
        }
    }

    public static MapleGroupData getData(String data) {
        return JsonUtils.parseObject(data, MapleGroupData.class, new MapleGroupData());
    }
}
