package org.xi.maple.datacalc.flink.sink;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.model.sink.UpsertKafkaSinkConfig;

public class UpsertKafkaSink extends MapleSink<UpsertKafkaSinkConfig> {

    public UpsertKafkaSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
