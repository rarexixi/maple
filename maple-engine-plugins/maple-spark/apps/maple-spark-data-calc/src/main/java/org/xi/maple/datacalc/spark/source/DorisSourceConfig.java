package org.xi.maple.datacalc.spark.source;

import org.xi.maple.datacalc.spark.model.SourceConfig;

import javax.validation.constraints.NotBlank;

public class DorisSourceConfig extends SourceConfig {

    @NotBlank
    private String fenodes;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotBlank
    private String database;

    @NotBlank
    private String table;

    public @NotBlank String getFenodes() {
        return fenodes;
    }

    public void setFenodes(@NotBlank String fenodes) {
        this.fenodes = fenodes;
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

    public @NotBlank String getDatabase() {
        return database;
    }

    public void setDatabase(@NotBlank String database) {
        this.database = database;
    }

    public @NotBlank String getTable() {
        return table;
    }

    public void setTable(@NotBlank String table) {
        this.table = table;
    }
}
