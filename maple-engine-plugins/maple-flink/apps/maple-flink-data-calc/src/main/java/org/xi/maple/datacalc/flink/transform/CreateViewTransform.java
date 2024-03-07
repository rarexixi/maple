package org.xi.maple.datacalc.flink.transform;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleTransform;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.NotBlank;
import java.util.Map;

public class CreateViewTransform extends MapleTransform<CreateViewTransform.Config> {

    public CreateViewTransform(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        tableEnv.createTemporaryView(config.getResultTable(), tableEnv.sqlQuery(config.selectSql));
    }

    @Data
    public static class Config extends MaplePluginConfig implements ResultTableConfig {
        String selectSql;

        String catalogName;
        String databaseName;
        @NotBlank
        String viewName;

        @Override
        public String getResultTable() {
            return TableUtils.getResultTable(catalogName, databaseName, viewName);
        }
    }
}
