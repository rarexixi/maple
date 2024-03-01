package org.xi.maple.datacalc.flink.transform;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

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