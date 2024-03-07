package org.xi.maple.datacalc.flink.sink;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;

import javax.validation.constraints.NotBlank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcSink extends MapleSink<JdbcSink.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSink.class);

    public JdbcSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    public void prepare() {
        try (Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())) {
            for (String query : config.getPreQueries()) {
                logger.info("Execute query: {}", query);
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    // statement.setQueryTimeout(jdbcOptions.queryTimeout)
                    int rows = statement.executeUpdate();
                    logger.info("{} rows affected", rows);
                }
            }
        } catch (Exception e) {
            logger.error("Execute query failed. ", e);
            throw new ConfigRuntimeException("Failed to execute pre-queries", e);
        }
    }

    @Data
    public static class Config extends MapleSink.SinkConfig {

        @NotBlank
        String url;
        @NotBlank
        String table;
        String username;
        String password;

        List<String> preQueries = new ArrayList<>();

        @Override
        public String getConnector() {
            return "jdbc";
        }

        @Override
        public Map<String, String> getDefineOptions() {
            Map<String, String> options = new HashMap<>();
            return options;
        }

        @Override
        public String getResultTable() {
            return null;
        }
    }
}
