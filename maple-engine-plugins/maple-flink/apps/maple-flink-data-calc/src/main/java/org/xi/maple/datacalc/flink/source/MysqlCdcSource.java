package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.MysqlCdcSourceConfig;

public class MysqlCdcSource extends MapleSource<MysqlCdcSourceConfig> {

    public MysqlCdcSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }

}
