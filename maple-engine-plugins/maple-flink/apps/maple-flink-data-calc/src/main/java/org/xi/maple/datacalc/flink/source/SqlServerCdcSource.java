package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.SqlServerCdcSourceConfig;

public class SqlServerCdcSource extends MapleSource<SqlServerCdcSourceConfig> {

    public SqlServerCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
