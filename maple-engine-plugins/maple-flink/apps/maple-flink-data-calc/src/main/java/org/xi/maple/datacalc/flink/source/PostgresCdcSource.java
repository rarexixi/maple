package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.PostgresCdcSourceConfig;

public class PostgresCdcSource extends MapleSource<PostgresCdcSourceConfig> {

    public PostgresCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
