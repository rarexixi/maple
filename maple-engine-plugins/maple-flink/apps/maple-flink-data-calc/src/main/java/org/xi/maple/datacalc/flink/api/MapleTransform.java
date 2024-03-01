package org.xi.maple.datacalc.flink.api;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import java.util.Map;

public abstract class MapleTransform<T extends MaplePluginConfig> extends MaplePlugin<T> implements TableDefine {

    public MapleTransform(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    public abstract void define();
}
