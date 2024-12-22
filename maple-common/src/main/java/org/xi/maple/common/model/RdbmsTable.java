package org.xi.maple.common.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class RdbmsTable implements Serializable {
    String database;
    String schema;
    String table;

    public String getTableIdentifier() {
        return StringUtils.isBlank(schema) ? table : schema + "." + table;
    }

    public String getTableIdentifierWithDb() {
        return StringUtils.isBlank(database) ? getTableIdentifier() : database + "." + getTableIdentifier();
    }
}
