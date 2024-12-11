package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcSource extends MapleSource<JdbcSource.Config> {

    public JdbcSource(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Data
    public static class Config extends MapleSource.SourceConfig {

        @NotBlank
        String url;
        @NotBlank
        String table;
        String username;
        String password;

        @Override
        public String getConnector() {
            return "jdbc";
        }

        @Override
        public Map<String, String> getDefineOptions() {
            Map<String, String> defineOptions = new LinkedHashMap<>();
            defineOptions.put("url", url);
            defineOptions.put("table-name", table);
            defineOptions.put("username", username);
            defineOptions.put("password", password);
            return defineOptions;
        }
    }
}
