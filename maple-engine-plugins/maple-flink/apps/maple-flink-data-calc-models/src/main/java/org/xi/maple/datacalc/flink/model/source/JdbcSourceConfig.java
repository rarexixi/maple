package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class JdbcSourceConfig extends SourceConfig {

    @NotBlank
    String jdbcUrl;
    String username;
    String password;
    @NotNull
    RdbmsTable rdbmsTable;

    @Override
    public String getConnector() {
        return "jdbc";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("url", jdbcUrl);
        defineOptions.put("username", username);
        defineOptions.put("password", password);
        defineOptions.put("table-name", rdbmsTable.getTableIdentifierWithDb());
        return defineOptions;
    }
}