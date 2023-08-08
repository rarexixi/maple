package org.xi.maple.datacalc.source;

import org.xi.maple.datacalc.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class FileSourceConfig extends SourceConfig {

    @NotBlank
    @Pattern(regexp = "^(file|hdfs)://.*", message = "Invalid path URI, please set the following allowed schemas: 'file://' or 'hdfs://'.")
    private String path;

    @NotBlank
    private String serializer = "parquet";

    private String[] columnNames;

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

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
