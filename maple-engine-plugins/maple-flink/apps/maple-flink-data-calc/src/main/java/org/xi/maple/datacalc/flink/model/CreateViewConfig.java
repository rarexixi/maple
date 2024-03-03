package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.NotBlank;

@Data
public class CreateViewConfig extends MaplePluginConfig implements ResultTableConfig {
    String catalogName;
    String databaseName;
    @NotBlank
    String viewName;

    String selectSql;

    @Override
    public String getResultTable() {
        return TableUtils.getResultTable(catalogName, databaseName, viewName);
    }
}