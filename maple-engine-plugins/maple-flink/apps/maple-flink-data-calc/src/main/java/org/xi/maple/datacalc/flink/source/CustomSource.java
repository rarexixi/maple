package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MaplePlugin;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.api.TableDefine;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.NotBlank;
import java.util.Map;

public class CustomSource extends MaplePlugin<CustomSource.Config> implements TableDefine {

    public CustomSource(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        tableEnv.executeSql(config.getCreateSql());
    }

    @Data
    public static class Config extends MaplePluginConfig implements ResultTableConfig {

        @NotBlank
        String createSql;

        @Override
        public String getResultTable() {
            return TableUtils.getTableName(createSql);
        }
    }
}
