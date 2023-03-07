package org.xi.maple.source;

import org.xi.maple.model.SourceConfig;

import javax.validation.constraints.NotBlank;

public class ManagedJdbcSourceConfig extends SourceConfig {

    @NotBlank
    private String datasource;

    @NotBlank
    private String query;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
