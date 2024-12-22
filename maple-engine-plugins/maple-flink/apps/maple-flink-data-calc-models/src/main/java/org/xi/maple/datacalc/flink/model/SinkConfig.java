package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Data
public abstract class SinkConfig extends StructTableConfig {
    protected String sourceTableName;

    protected String sourceSql;

    @AssertTrue(message = "[sourceSql, sourceTableName] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(sourceSql) || StringUtils.isNotBlank(sourceTableName);
    }
}