package org.xi.maple.datacalc.spark.source;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.spark.model.SourceConfig;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

public class ManagedJdbcSourceConfig extends SourceConfig {

    @NotBlank
    private String datasource;

    private String table;

    private String query;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
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
