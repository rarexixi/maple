package org.xi.maple.datacalc.flink.sink;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.model.sink.DorisSinkConfig;

public class DorisSink extends MapleSink<DorisSinkConfig> {

    public DorisSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
