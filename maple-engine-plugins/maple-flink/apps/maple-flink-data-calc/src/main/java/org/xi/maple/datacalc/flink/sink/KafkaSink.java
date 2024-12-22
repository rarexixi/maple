package org.xi.maple.datacalc.flink.sink;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.model.sink.KafkaSinkConfig;

public class KafkaSink extends MapleSink<KafkaSinkConfig> {

    public KafkaSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
