package org.xi.maple.datacalc.flink.sink;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.*;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import java.util.Map;

public class CustomSink extends MaplePlugin<CustomSink.Config> implements TableDefine, TableInsert {

    public CustomSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        tableEnv.executeSql(config.getSql());
    }

    @Override
    public String getInsertSql() {
        return null;
    }

    @Data
    public static class Config extends MaplePluginConfig implements ResultTableConfig {
        String sql;

        @Override
        public String getResultTable() {
            return null;
        }
    }
}
