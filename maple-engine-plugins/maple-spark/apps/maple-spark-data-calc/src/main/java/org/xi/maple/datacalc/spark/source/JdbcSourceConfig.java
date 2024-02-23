package org.xi.maple.datacalc.spark.source;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.spark.model.SourceConfig;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

public class JdbcSourceConfig extends SourceConfig {

    @NotBlank
    private String url;

    @NotBlank
    private String driver;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    private String table;

    private String query;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @AssertTrue(message = "[table, query] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(table) || StringUtils.isNotBlank(query);
    }
}
