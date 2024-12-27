package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Data
public abstract class SinkConfig extends StructTableConfig {
    protected String sourceTable;

    protected String sourceQuery;

    @AssertTrue(message = "[sourceQuery, sourceTable] cannot be blank at the same time.")
    public boolean isSourceOK() {
        return StringUtils.isNotBlank(sourceQuery) || StringUtils.isNotBlank(sourceTable);
    }
}