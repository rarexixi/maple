package org.xi.maple.datacalc.sink;

import org.xi.maple.datacalc.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class ManagedJdbcSinkConfig extends SinkConfig {

    @NotBlank
    private String targetDatasource;

    @NotBlank
    private String targetDatabase;

    @NotBlank
    private String targetTable;

    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";

    private List<String> preQueries = new ArrayList<>();

    private Integer numPartitions = 10;

    public String getTargetDatasource() {
        return targetDatasource;
    }

    public void setTargetDatasource(String targetDatasource) {
        this.targetDatasource = targetDatasource;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public List<String> getPreQueries() {
        return preQueries;
    }

    public void setPreQueries(List<String> preQueries) {
        this.preQueries = preQueries;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        if (numPartitions == null) return;
        this.numPartitions = numPartitions > 20 ? 20 : numPartitions;
    }
}
