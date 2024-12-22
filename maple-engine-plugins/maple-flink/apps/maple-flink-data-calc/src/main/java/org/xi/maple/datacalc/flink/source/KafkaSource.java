package org.xi.maple.datacalc.flink.source;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;
import org.xi.maple.datacalc.flink.model.source.KafkaSourceConfig;

public class KafkaSource extends MapleSource<KafkaSourceConfig> {

    public KafkaSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
