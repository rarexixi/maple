package org.xi.maple.datacalc.spark.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public abstract class SinkConfig extends MaplePluginConfig implements Serializable {

    protected String sourceTable;

    protected String sourceQuery;

    private Integer numPartitions;

    private Map<String, String> options = Collections.emptyMap();

    @AssertTrue(message = "[sourceTable, sourceQuery] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(sourceTable) || StringUtils.isNotBlank(sourceQuery);
    }

    public void setOptions(Map<String, String> options) {
        this.options = Optional.ofNullable(options).orElse(this.options);
    }
}
