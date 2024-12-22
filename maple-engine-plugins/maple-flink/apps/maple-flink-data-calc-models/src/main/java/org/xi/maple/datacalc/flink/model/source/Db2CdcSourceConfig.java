package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Db2CdcSourceConfig extends SourceConfig {

    @NotBlank
    String hostname;
    @NotBlank
    String port = "50000";
    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    RdbmsTable rdbmsTable;
    @Pattern(regexp = "initial|latest-offset")
    String scanStartupMode = "initial";

    @Override
    public String getConnector() {
        return "db2-cdc";
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
        defineOptions.put("scan.startup.mode", scanStartupMode);
        return defineOptions;
    }
}