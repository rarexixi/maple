package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class SqlServerCdcSourceConfig extends SourceConfig {

    @NotBlank
    String hostname;
    @NotBlank
    String port = "1433";
    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    RdbmsTable rdbmsTable;

    @Override
    public String getConnector() {
        return "sqlserver-cdc";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("hostname", hostname);
        defineOptions.put("port", port);
        defineOptions.put("username", username);
        defineOptions.put("password", password);
        defineOptions.put("database-name", rdbmsTable.getDatabase());
        defineOptions.put("table-name", rdbmsTable.getTableIdentifier());
        return defineOptions;
    }
}