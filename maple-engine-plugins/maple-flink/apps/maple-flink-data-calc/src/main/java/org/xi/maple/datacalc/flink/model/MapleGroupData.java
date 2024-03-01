package org.xi.maple.datacalc.flink.model;

import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.util.Optional;

public class MapleGroupData extends MapleData implements Serializable {

    private MapleDataConfig[] sources = new MapleDataConfig[0];
    private MapleDataConfig[] transformations = new MapleDataConfig[0];
    private MapleDataConfig[] sinks = new MapleDataConfig[0];

    public MapleDataConfig[] getSources() {
        return sources;
    }

    public void setSources(MapleDataConfig[] sources) {
        this.sources = Optional.ofNullable(sources).orElse(this.sources);
    }

    public MapleDataConfig[] getTransformations() {
        return transformations;
    }

    public void setTransformations(MapleDataConfig[] transformations) {
        this.transformations = Optional.ofNullable(transformations).orElse(this.transformations);
    }

    public MapleDataConfig[] getSinks() {
        return sinks;
    }

    public void setSinks(MapleDataConfig[] sinks) {
        this.sinks = Optional.ofNullable(sinks).orElse(this.sinks);
    }

    public static MapleGroupData getData(String data) {
        return JsonUtils.parseObject(data, MapleGroupData.class, new MapleGroupData());
    }
}
