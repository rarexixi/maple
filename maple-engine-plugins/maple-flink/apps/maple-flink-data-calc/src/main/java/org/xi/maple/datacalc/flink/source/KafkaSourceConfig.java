package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.flink.model.CreateTableConfig;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class KafkaSourceConfig extends CreateTableConfig {

    @NotBlank
    String bootstrapServers;

    String topic;
    String topicPattern;

    @NotBlank
    String groupId;

    @NotBlank
    String format;

    @AssertTrue(message = "[topic, topicPattern] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(topic) || StringUtils.isNotBlank(topicPattern);
    }

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
