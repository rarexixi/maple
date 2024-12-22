package org.xi.maple.datacalc.flink.model.sink;

import lombok.Data;
import org.xi.maple.datacalc.flink.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class UpsertKafkaSinkConfig extends SinkConfig {

    @NotBlank
    String bootstrapServers;

    @NotBlank
    String topic;

    @NotBlank
    String keyFormat;

    @NotBlank
    String valueFormat;

    @NotBlank
    @Pattern(regexp = "ALL|EXCEPT_KEY")
    String valueFieldsInclude = "ALL";

    @Override
    public String getConnector() {
        return "kafka";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("topic", topic);
        defineOptions.put("properties.bootstrap.servers", bootstrapServers);
        defineOptions.put("key.format", keyFormat);
        defineOptions.put("value.format", valueFormat);
        defineOptions.put("value.fields-include", valueFieldsInclude);
        return defineOptions;
    }
}