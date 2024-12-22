package org.xi.maple.datacalc.flink.model.source;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DorisSourceConfig extends SourceConfig {
    @NotBlank
    String fenodes;
    String jdbcUrl;
    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    RdbmsTable rdbmsTable;

    @Override
    public String getConnector() {
        return "doris";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("fenodes", fenodes);
        if (StringUtils.isNotBlank(jdbcUrl)) defineOptions.put("jdbc-url", jdbcUrl);
        defineOptions.put("username", username);
        defineOptions.put("password", password);
        defineOptions.put("table.identifier", rdbmsTable.getTableIdentifierWithDb());
        return defineOptions;
    }
}