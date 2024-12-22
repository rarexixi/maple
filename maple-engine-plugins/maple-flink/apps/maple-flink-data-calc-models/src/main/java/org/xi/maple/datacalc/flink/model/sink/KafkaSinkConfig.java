package org.xi.maple.datacalc.flink.model.sink;

import lombok.Data;
import org.xi.maple.datacalc.flink.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class KafkaSinkConfig extends SinkConfig {

    @NotBlank
    String bootstrapServers;

    @NotBlank
    String topic;

    @NotBlank
    String format;

    @Override
    public String getConnector() {
        return "kafka";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("properties.bootstrap.servers", bootstrapServers);
        defineOptions.put("topic", topic);
        defineOptions.put("format", format);
        return defineOptions;
    }
}