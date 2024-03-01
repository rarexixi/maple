package org.xi.maple.datacalc.flink.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public abstract class InsertTableConfig extends CreateTableConfig {

    String sourceCatalogName;
    String sourceDatabaseName;
    @NotBlank
    String sourceTableName;
}
