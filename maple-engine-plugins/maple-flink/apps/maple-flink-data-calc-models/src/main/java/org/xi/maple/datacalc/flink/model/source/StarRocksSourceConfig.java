package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

    @Data
    public  class StarRocksSourceConfig extends SourceConfig {

        @NotBlank
        String fenodes;
        String jdbcUrl;
        @NotBlank
        String username;
        @NotBlank
        String password;
        @NotNull
        RdbmsTable rdbmsTable;

        @Override
        public String getConnector() {
            return "starrocks";
        }

        @Override
        public Map<String, String> getDefineOptions() {
            Map<String, String> defineOptions = new LinkedHashMap<>();
            defineOptions.put("scan-url", fenodes);
            defineOptions.put("jdbc-url", jdbcUrl);
            defineOptions.put("username", username);
            defineOptions.put("password", password);
            defineOptions.put("database-name", rdbmsTable.getDatabase());
            defineOptions.put("table-name", rdbmsTable.getTable());
            return defineOptions;
        }
    }