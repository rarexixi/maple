package org.xi.maple.datacalc.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Optional;

public abstract class TransformConfig extends MaplePluginConfig implements ResultTableConfig, Serializable {

    protected String sourceTable;

    @NotBlank
    protected String resultTable;

    private Boolean persist = false;

    @NotBlank
    @Pattern(regexp = "^(MEMORY_ONLY|MEMORY_AND_DISK|MEMORY_ONLY_SER|MEMORY_AND_DISK_SER|DISK_ONLY|MEMORY_ONLY_2|MEMORY_AND_DISK_2|MEMORY_ONLY_SER_2|MEMORY_AND_DISK_SER_2|DISK_ONLY_2|OFF_HEAP)$",
            message = "Unknown storageLevel: {saveMode}. Accepted save modes are 'MEMORY_ONLY','MEMORY_AND_DISK','MEMORY_ONLY_SER','MEMORY_AND_DISK_SER','DISK_ONLY','MEMORY_ONLY_2','MEMORY_AND_DISK_2','MEMORY_ONLY_SER_2','MEMORY_AND_DISK_SER_2','DISK_ONLY_2','OFF_HEAP'.")
    private String storageLevel = "MEMORY_AND_DISK";

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

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
        this.storageLevel = Optional.ofNullable(storageLevel).orElse(this.storageLevel);
    }
}
