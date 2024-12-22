package org.xi.maple.jdbc.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "maple.jdbc")
@Data
public class MapleJdbcProperties {
    private String getDatabasesSql = "";
    private String getSchemasSql = "";
    private String getTablesSql = "";
    private String getTableColumnsSql = "";
    private String getTablePkColumnsSql = "";
}