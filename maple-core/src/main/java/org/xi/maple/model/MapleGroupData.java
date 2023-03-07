package org.xi.maple.model;

import org.xi.maple.util.JsonUtils;

import java.io.Serializable;

public class MapleGroupData extends MaplePluginConfig implements Serializable {

    private MapleDataConfig[] sources = new MapleDataConfig[0];
    private MapleDataConfig[] transformations = new MapleDataConfig[0];
    private MapleDataConfig[] sinks = new MapleDataConfig[0];

    public MapleDataConfig[] getSources() {
        return sources;
    }

    public void setSources(MapleDataConfig[] sources) {
        this.sources = sources;
    }

    public MapleDataConfig[] getTransformations() {
        return transformations;
    }

    public void setTransformations(MapleDataConfig[] transformations) {
        this.transformations = transformations;
    }

    public MapleDataConfig[] getSinks() {
        return sinks;
    }

    public void setSinks(MapleDataConfig[] sinks) {
        this.sinks = sinks;
    }

    public static MapleGroupData getData(String data) {
        return JsonUtils.parseObject(data, MapleGroupData.class, new MapleGroupData());
    }
}
