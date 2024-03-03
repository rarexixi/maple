package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.NotBlank;

@Data
public abstract class InsertTableConfig extends CreateTableConfig {

    String sourceCatalogName;
    String sourceDatabaseName;
    @NotBlank
    String sourceTableName;

    public String getSelectSourceTable() {
        String resultTable = TableUtils.getResultTable(sourceCatalogName, sourceDatabaseName, sourceTableName);
        return String.format("SELECT * FROM %s", resultTable);
    }

    public String getInsertSql() {
        String sourceTable = TableUtils.getResultTable(sourceCatalogName, sourceDatabaseName, sourceTableName);
        return String.format("INSERT INTO %s SELECT * FROM %s", getResultTable(), sourceTable);
    }
}
