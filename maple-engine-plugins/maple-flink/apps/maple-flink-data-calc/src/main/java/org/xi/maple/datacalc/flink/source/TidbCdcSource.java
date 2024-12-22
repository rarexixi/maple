package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.TidbCdcSourceConfig;

public class TidbCdcSource extends MapleSource<TidbCdcSourceConfig> {

    public TidbCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
