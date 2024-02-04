package org.xi.maple.datacalc.model;

import org.xi.maple.datacalc.util.VariableUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class SourceConfig extends MaplePluginConfig implements ResultTableConfig, Serializable {

    @NotBlank
    protected String resultTable;

    private Boolean persist = false;

    @NotBlank
    @Pattern(regexp = "^(MEMORY_ONLY|MEMORY_AND_DISK|MEMORY_ONLY_SER|MEMORY_AND_DISK_SER|DISK_ONLY|MEMORY_ONLY_2|MEMORY_AND_DISK_2|MEMORY_ONLY_SER_2|MEMORY_AND_DISK_SER_2|DISK_ONLY_2|OFF_HEAP)$",
            message = "Unknown storageLevel: {saveMode}. Accepted save modes are 'MEMORY_ONLY','MEMORY_AND_DISK','MEMORY_ONLY_SER','MEMORY_AND_DISK_SER','DISK_ONLY','MEMORY_ONLY_2','MEMORY_AND_DISK_2','MEMORY_ONLY_SER_2','MEMORY_AND_DISK_SER_2','DISK_ONLY_2','OFF_HEAP'.")
    private String storageLevel = "MEMORY_AND_DISK";

    private Map<String, String> options = Collections.emptyMap();

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
        this.storageLevel = VariableUtils.getNotNullValue(storageLevel, this.storageLevel);
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = VariableUtils.getNotNullValue(options, this.options);
    }
}
