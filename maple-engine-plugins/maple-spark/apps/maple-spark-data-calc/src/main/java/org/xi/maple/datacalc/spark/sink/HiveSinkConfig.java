package org.xi.maple.datacalc.spark.sink;

import org.xi.maple.datacalc.spark.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class HiveSinkConfig extends SinkConfig {

    @NotBlank
    private String targetDatabase;

    @NotBlank
    private String targetTable;

    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";

    private Boolean strongCheck = true;

    private Boolean writeAsFile = false;

    protected Map<String, String> variables = Collections.emptyMap();

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

    public Boolean getStrongCheck() {
        return strongCheck;
    }

    public void setStrongCheck(Boolean strongCheck) {
        this.strongCheck = strongCheck;
    }

    public Boolean getWriteAsFile() {
        return writeAsFile;
    }

    public void setWriteAsFile(Boolean writeAsFile) {
        this.writeAsFile = writeAsFile;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = Optional.ofNullable(variables).orElse(this.variables);
    }
}
