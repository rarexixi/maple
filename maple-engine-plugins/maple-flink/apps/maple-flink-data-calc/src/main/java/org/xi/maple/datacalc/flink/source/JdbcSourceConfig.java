package org.xi.maple.datacalc.flink.source;

import lombok.Data;
import org.xi.maple.datacalc.flink.model.CreateTableConfig;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class JdbcSourceConfig extends CreateTableConfig {

    @NotBlank
    String url;
    @NotBlank
    String table;
    String username;
    String password;

    @Override
    public String getConnector() {
        return "jdbc";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        return null;
    }

    @Override
    public String getResultTable() {
        return null;
    }
}
