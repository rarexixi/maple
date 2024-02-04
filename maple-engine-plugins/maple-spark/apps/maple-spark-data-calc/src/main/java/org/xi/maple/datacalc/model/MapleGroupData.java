package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.JsonUtils;
import org.xi.maple.datacalc.util.VariableUtils;

import java.io.Serializable;

public class MapleGroupData extends MapleData implements Serializable {

    private MapleDataConfig[] sources = new MapleDataConfig[0];
    private MapleDataConfig[] transformations = new MapleDataConfig[0];
    private MapleDataConfig[] sinks = new MapleDataConfig[0];

    public MapleDataConfig[] getSources() {
        return sources;
    }

    public void setSources(MapleDataConfig[] sources) {
        this.sources = VariableUtils.getNotNullValue(sources, this.sources);
    }

    public MapleDataConfig[] getTransformations() {
        return transformations;
    }

    public void setTransformations(MapleDataConfig[] transformations) {
        this.transformations = VariableUtils.getNotNullValue(transformations, this.transformations);
    }

    public MapleDataConfig[] getSinks() {
        return sinks;
    }

    public void setSinks(MapleDataConfig[] sinks) {
        this.sinks = VariableUtils.getNotNullValue(sinks, this.sinks);
    }

    public static MapleGroupData getData(String data) {
        return JsonUtils.parseObject(data, MapleGroupData.class, new MapleGroupData());
    }
}
