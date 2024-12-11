package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleSource;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

public class KafkaSource extends MapleSource<KafkaSource.Config> {

    public KafkaSource(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Data
    public static class Config extends MapleSource.SourceConfig {

        @NotBlank
        String bootstrapServers;

        @NotBlank
        String topic;

        String groupId;

        @NotBlank
        String format;

        @Override
        public String getConnector() {
            return "kafka";
        }

        @Override
        public Map<String, String> getDefineOptions() {
            Map<String, String> defineOptions = new LinkedHashMap<>();
            defineOptions.put("topic", topic);
            defineOptions.put("properties.bootstrap.servers", bootstrapServers);
            if (StringUtils.isNoneBlank(groupId)) {
                defineOptions.put("properties.group.id", groupId);
            }
            defineOptions.put("format", format);
            return defineOptions;
        }
    }
}
