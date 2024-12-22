package org.xi.maple.datacalc.spark.model.source;

import lombok.Data;
import org.xi.maple.datacalc.spark.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class FileSourceConfig extends SourceConfig {

    @NotBlank
    @Pattern(regexp = "^(file|hdfs)://.*", message = "Invalid path URI, please set the following allowed schemas: 'file://' or 'hdfs://'.")
    private String path;

    @NotBlank
    private String serializer = "parquet";

    private String[] columnNames;
}
