package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.OceanBaseCdcSourceConfig;

public class OceanBaseCdcSource extends MapleSource<OceanBaseCdcSourceConfig> {

    public OceanBaseCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
