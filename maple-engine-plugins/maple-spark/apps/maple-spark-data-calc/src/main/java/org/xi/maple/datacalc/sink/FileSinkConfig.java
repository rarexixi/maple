package org.xi.maple.datacalc.sink;

import org.xi.maple.datacalc.model.SinkConfig;
import org.xi.maple.datacalc.util.VariableUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;

public class FileSinkConfig extends SinkConfig {

    @NotBlank
    @Pattern(regexp = "^(file|hdfs)://.*", message = "Invalid path URI, please set the following allowed schemas: 'file://' or 'hdfs://'.")
    private String path;

    @NotBlank
    private String serializer = "parquet";

    private List<String> partitionBy = Collections.emptyList();

    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public List<String> getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(List<String> partitionBy) {
        this.partitionBy = VariableUtils.getNotNullValue(partitionBy, this.partitionBy);
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }
}
