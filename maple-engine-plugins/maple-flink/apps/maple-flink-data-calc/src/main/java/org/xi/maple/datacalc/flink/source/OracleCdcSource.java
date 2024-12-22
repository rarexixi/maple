package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.OracleCdcSourceConfig;

public class OracleCdcSource extends MapleSource<OracleCdcSourceConfig> {

    public OracleCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
