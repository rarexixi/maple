package org.xi.maple.datacalc.flink.transform;

import lombok.Data;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.NotBlank;

@Data
public class SqlTransformConfig extends MaplePluginConfig implements ResultTableConfig {
    String sql;

    String catalogName;
    String databaseName;
    @NotBlank
    String viewName;

    @Override
    public String getResultTable() {
        return TableUtils.getResultTable(catalogName, databaseName, viewName);
    }
}