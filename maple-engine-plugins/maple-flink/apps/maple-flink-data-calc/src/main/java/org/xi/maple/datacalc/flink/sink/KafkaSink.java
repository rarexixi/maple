package org.xi.maple.datacalc.flink.sink;

import lombok.Data;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSink;

import javax.validation.constraints.NotBlank;
import java.util.Map;

public class KafkaSink extends MapleSink<KafkaSink.Config> {

    public KafkaSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Data
    public static class Config extends MapleSink.SinkConfig {

        @NotBlank
        String bootstrapServers;

        @NotBlank
        String topic;

        @NotBlank
        String groupId;

        @NotBlank
        String format;

        @Override
        public String getConnector() {
            return "kafka";
        }

        @Override
        public Map<String, String> getDefineOptions() {
            return null;
        }

        @Override
        public String getResultTable() {
            return null;
        }
    }
}
