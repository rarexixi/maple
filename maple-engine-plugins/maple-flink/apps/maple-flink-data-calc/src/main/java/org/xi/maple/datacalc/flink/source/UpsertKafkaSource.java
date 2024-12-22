package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.UpsertKafkaSourceConfig;

public class UpsertKafkaSource extends MapleSource<UpsertKafkaSourceConfig> {

    public UpsertKafkaSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
