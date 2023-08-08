package org.xi.maple.datacalc.model;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class SourceConfig extends MaplePluginConfig implements ResultTableConfig, Serializable {

    @NotBlank
    protected String resultTable;

    private Boolean persist = false;

    private String storageLevel = "MEMORY_AND_DISK";

    private Map<String, String> options = new HashMap<>();

    public String getResultTable() {
        return resultTable;
    }

    public void setResultTable(String resultTable) {
        this.resultTable = resultTable;
    }

    public Boolean getPersist() {
        return persist;
    }

    public void setPersist(Boolean persist) {
        this.persist = persist;
    }

    public String getStorageLevel() {
        return storageLevel;
    }

    public void setStorageLevel(String storageLevel) {
        if (StringUtils.isNotBlank(storageLevel)) this.storageLevel = storageLevel;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
