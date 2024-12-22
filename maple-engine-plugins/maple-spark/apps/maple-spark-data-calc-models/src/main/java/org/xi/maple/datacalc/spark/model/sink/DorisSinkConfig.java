package org.xi.maple.datacalc.spark.model.sink;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.spark.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DorisSinkConfig extends SinkConfig {

    @NotBlank
    private String fenodes;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotNull
    private RdbmsTable targetTable;

    // 从 1.3.0 版本开始，支持 overwrite 模式写入（只支持全表级别的数据覆盖）
    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";
}
