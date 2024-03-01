package org.xi.maple.datacalc.flink.transform;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleTransform;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import javax.validation.constraints.NotBlank;
import java.util.Map;

public class SqlTransform extends MapleTransform<SqlTransform.Config> {

    public SqlTransform(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        tableEnv.createTemporaryView(config.getResultTable(), tableEnv.sqlQuery(config.getSql()));
    }

    @Data
    public static class Config extends MaplePluginConfig implements ResultTableConfig {
        String sql;

        String catalogName;
        String databaseName;
        @NotBlank
        String viewName;

        @Override
        public String getResultTable() {
            StringBuilder viewNameBuilder = new StringBuilder();
            if (StringUtils.isNotBlank(catalogName)) {
                viewNameBuilder.append(catalogName).append(".");
            }

            if (StringUtils.isNotBlank(databaseName)) {
                viewNameBuilder.append(databaseName).append(".");
            }

            viewNameBuilder.append(viewName);
            return viewNameBuilder.toString();
        }
    }
}
