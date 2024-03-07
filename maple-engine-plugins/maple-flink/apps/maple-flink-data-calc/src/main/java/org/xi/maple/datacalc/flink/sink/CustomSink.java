package org.xi.maple.datacalc.flink.sink;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.*;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import java.util.Map;

public class CustomSink extends MaplePlugin<CustomSink.Config> implements TableDefine, TableInsert {

    public CustomSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        tableEnv.executeSql(config.getCreateSql());
    }

    @Override
    public String getInsertSql() {
        return null;
    }

    @Data
    public static class Config extends MaplePluginConfig implements ResultTableConfig, TableInsert {
        String createSql;
        String insertSql;

        @Override
        public String getResultTable() {
            return TableUtils.getTableName(createSql);
        }

        @Override
        public String getInsertSql() {
            return insertSql;
        }
    }
}
