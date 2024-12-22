package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.Db2CdcSourceConfig;

public class Db2CdcSource extends MapleSource<Db2CdcSourceConfig> {

    public Db2CdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }

}
