package org.xi.maple.datacalc.transform;

import org.xi.maple.datacalc.model.TransformConfig;

import javax.validation.constraints.NotBlank;

public class SqlTransformConfig extends TransformConfig {

    @NotBlank
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
