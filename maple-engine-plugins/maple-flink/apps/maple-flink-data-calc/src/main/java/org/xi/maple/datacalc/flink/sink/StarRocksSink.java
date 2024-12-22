package org.xi.maple.datacalc.flink.sink;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSink;
import org.xi.maple.datacalc.flink.model.sink.StarRocksSinkConfig;

public class StarRocksSink extends MapleSink<StarRocksSinkConfig> {

    public StarRocksSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }
}
