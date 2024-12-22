package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.StarRocksSourceConfig;

public class StarRocksSource extends MapleSource<StarRocksSourceConfig> {

    public StarRocksSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
