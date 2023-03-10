package org.xi.maple.model;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class SinkConfig extends MaplePluginConfig implements Serializable {

    protected String sourceTable;

    protected String sourceQuery;

    private Map<String, String> options = new HashMap<>();

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

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
