package org.xi.maple.datacalc.spark.model.sink;

import lombok.Data;
import org.xi.maple.datacalc.spark.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
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

    public void setPartitionBy(List<String> partitionBy) {
        this.partitionBy = Optional.ofNullable(partitionBy).orElse(this.partitionBy);
    }
}
