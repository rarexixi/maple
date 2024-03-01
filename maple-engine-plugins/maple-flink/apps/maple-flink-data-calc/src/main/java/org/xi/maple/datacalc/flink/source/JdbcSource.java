package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;

import javax.validation.constraints.NotBlank;
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
            return null;
        }

        @Override
        public String getResultTable() {
            return null;
        }
    }
}
