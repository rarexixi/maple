package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class KafkaSourceConfig extends SourceConfig {

    @NotBlank
    String bootstrapServers;

    @NotBlank
    String topic;

    String groupId;

    @NotBlank
    String format;

    @Pattern(regexp = "earliest-offset|latest-offset|group-offset|timestamp|specific-offset")
    String scanStartupMode = "group-offsets";

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
        defineOptions.put("scan.startup.mode", scanStartupMode);
        defineOptions.put("format", format);
        return defineOptions;
    }
}