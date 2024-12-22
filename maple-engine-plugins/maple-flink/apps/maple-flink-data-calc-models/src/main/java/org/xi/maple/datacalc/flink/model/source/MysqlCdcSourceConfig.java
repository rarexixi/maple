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
public class MysqlCdcSourceConfig extends SourceConfig {

    @NotBlank
    String hostname;
    @NotBlank
    String port = "3306";
    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    RdbmsTable rdbmsTable;
    @Pattern(regexp = "initial|earliest-offset|latest-offset|specific-offset|timestamp|snapshot")
    String scanStartupMode = "initial";

    @Override
    public String getConnector() {
        return "mysql-cdc";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("hostname", hostname);
        defineOptions.put("port", port);
        defineOptions.put("username", username);
        defineOptions.put("password", password);
        defineOptions.put("database-name", rdbmsTable.getDatabase());
        defineOptions.put("table-name", rdbmsTable.getTable());
        defineOptions.put("scan.startup.mode", scanStartupMode);
        return defineOptions;
    }
}