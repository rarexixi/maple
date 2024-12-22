package org.xi.maple.datacalc.flink.api;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

@Data
public abstract class MaplePlugin<T extends MaplePluginConfig> implements TableDefine {

    protected final TableEnvironment tableEnv;
    protected T config;

    public MaplePlugin(TableEnvironment tableEnv) {
        this.tableEnv = tableEnv;
    }
}
