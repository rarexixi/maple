package org.xi.maple.datacalc.flink.api;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import java.util.Map;

@Data
public abstract class MaplePlugin<T extends MaplePluginConfig> {

    protected final TableEnvironment tableEnv;
    protected final Map<String, String> gv;

    public MaplePlugin(TableEnvironment tableEnv, Map<String, String> gv) {
        this.tableEnv = tableEnv;
        this.gv = gv;
    }

    protected T config;
}
