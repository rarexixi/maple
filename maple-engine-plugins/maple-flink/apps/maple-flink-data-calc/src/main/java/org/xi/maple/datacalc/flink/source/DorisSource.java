package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.DorisSourceConfig;

public class DorisSource extends MapleSource<DorisSourceConfig> {

    public DorisSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
