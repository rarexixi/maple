package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.JdbcSourceConfig;

public class JdbcSource extends MapleSource<JdbcSourceConfig> {

    public JdbcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }

}
