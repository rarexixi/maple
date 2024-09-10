package org.xi.maple.datacalc.spark.sink;

import org.xi.maple.datacalc.spark.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class StarRocksSinkConfig extends SinkConfig {

    @NotBlank
    private String feHttpUrl;

    @NotBlank
    private String feJdbcUrl;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotBlank
    private String targetDatabase;

    @NotBlank
    private String targetTable;

    // 从 1.3.0 版本开始，支持 overwrite 模式写入（只支持全表级别的数据覆盖）
    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";

    public @NotBlank String getFeHttpUrl() {
        return feHttpUrl;
    }

    public void setFeHttpUrl(@NotBlank String feHttpUrl) {
        this.feHttpUrl = feHttpUrl;
    }

    public @NotBlank String getFeJdbcUrl() {
        return feJdbcUrl;
    }

    public void setFeJdbcUrl(@NotBlank String feJdbcUrl) {
        this.feJdbcUrl = feJdbcUrl;
    }

    public @NotBlank String getUser() {
        return user;
    }

    public void setUser(@NotBlank String user) {
        this.user = user;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public @NotBlank String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(@NotBlank String targetTable) {
        this.targetTable = targetTable;
    }

    public @NotBlank @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.") String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(@NotBlank @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.") String saveMode) {
        this.saveMode = saveMode;
    }
}
