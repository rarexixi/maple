package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import javax.validation.constraints.NotBlank;


@Data
public class CustomSourceConfig extends MaplePluginConfig implements ResultTableConfig {

    @NotBlank
    String sql;

    @Override
    public String getResultTable() {
        return null;
    }
}