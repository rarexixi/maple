package org.xi.maple.datacalc.spark.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public abstract class SourceConfig extends MaplePluginConfig implements ResultTableConfig, Serializable {

    @NotBlank
    protected String resultTable;

    private Boolean persist = false;

    @NotBlank
    @Pattern(regexp = "^(MEMORY_ONLY|MEMORY_AND_DISK|MEMORY_ONLY_SER|MEMORY_AND_DISK_SER|DISK_ONLY|MEMORY_ONLY_2|MEMORY_AND_DISK_2|MEMORY_ONLY_SER_2|MEMORY_AND_DISK_SER_2|DISK_ONLY_2|OFF_HEAP)$",
            message = "Unknown storageLevel: {saveMode}. Accepted save modes are 'MEMORY_ONLY','MEMORY_AND_DISK','MEMORY_ONLY_SER','MEMORY_AND_DISK_SER','DISK_ONLY','MEMORY_ONLY_2','MEMORY_AND_DISK_2','MEMORY_ONLY_SER_2','MEMORY_AND_DISK_SER_2','DISK_ONLY_2','OFF_HEAP'.")
    private String storageLevel = "MEMORY_AND_DISK";

    private Map<String, String> options = Collections.emptyMap();

    public void setStorageLevel(String storageLevel) {
        this.storageLevel = Optional.ofNullable(storageLevel).orElse(this.storageLevel);
    }

    public void setOptions(Map<String, String> options) {
        this.options = Optional.ofNullable(options).orElse(this.options);
    }
}
