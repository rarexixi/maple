package org.xi.maple.datacalc.flink.sink;

import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.sink.JdbcSinkConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcSink extends MapleSink<JdbcSinkConfig> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSink.class);

    public JdbcSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }

    @Override
    public void prepare() {
        super.prepare();
        try (Connection conn = DriverManager.getConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword())) {
            for (String query : config.getPreQueries()) {
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

}
