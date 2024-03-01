package org.xi.maple.datacalc.flink.sink;

import lombok.Data;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

@Data
public class CustomSinkConfig extends MaplePluginConfig implements ResultTableConfig {
    String sql;

    @Override
    public String getResultTable() {
        return null;
    }
}