package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;

@Data
public abstract class CustomInsertTableConfig extends MaplePluginConfig implements ResultTableConfig {

    String insertSql;
}
