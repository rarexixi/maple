package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;

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
        StringBuilder viewNameBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(catalogName)) {
            viewNameBuilder.append(catalogName).append(".");
        }

        if (StringUtils.isNotBlank(databaseName)) {
            viewNameBuilder.append(databaseName).append(".");
        }

        viewNameBuilder.append(viewName);
        return viewNameBuilder.toString();
    }
}