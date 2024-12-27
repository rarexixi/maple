package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.model.definition.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class StructTableConfig extends MaplePluginConfig {

    protected String comment;
    @NotEmpty
    @Valid
    protected List<PhysicalColumn> physicalColumns = new ArrayList<>();
    protected List<MetadataColumn> metadataColumns = new ArrayList<>();
    protected List<ComputedColumn> computedColumns = new ArrayList<>();
    protected String pkName;
    protected String[] pkColumns;

    protected String wmColumn;
    protected Integer wmDelaySeconds;

    protected String[] partitionColumns;

    protected Map<String, String> options = new LinkedHashMap<>();

    public String getWatermarkExpression() {
        return String.format("%s - INTERVAL '%d' SECOND", wmColumn, wmDelaySeconds);
    }

    public Map<String, String> getOptions() {
        final Map<String, String> result = new LinkedHashMap<>();
        result.put("connector", getConnector());
        result.putAll(getDefineOptions());
        options.forEach((key, value) -> {
            if (!"connector".equals(key) && !result.containsKey(key)) {
                result.put(key, value);
            }
        });
        return result;
    }

    public void setOptions(Map<String, String> options) {
        if (options == null) {
            return;
        }
        this.options.putAll(options);
    }

    public abstract String getConnector();

    public abstract Map<String, String> getDefineOptions();
}
