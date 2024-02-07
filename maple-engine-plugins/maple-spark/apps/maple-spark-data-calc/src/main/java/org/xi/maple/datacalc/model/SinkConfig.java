package org.xi.maple.datacalc.model;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class SinkConfig extends MaplePluginConfig implements Serializable {

    protected String sourceTable;

    protected String sourceQuery;

    private Integer numPartitions;

    private Map<String, String> options = Collections.emptyMap();

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceQuery() {
        return sourceQuery;
    }

    public void setSourceQuery(String sourceQuery) {
        this.sourceQuery = sourceQuery;
    }

    @AssertTrue(message = "[sourceTable, sourceQuery] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(sourceTable) || StringUtils.isNotBlank(sourceQuery);
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = Optional.ofNullable(options).orElse(this.options);
    }
}
