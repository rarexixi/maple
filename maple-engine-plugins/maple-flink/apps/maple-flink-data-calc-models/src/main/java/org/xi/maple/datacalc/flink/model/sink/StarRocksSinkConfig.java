package org.xi.maple.datacalc.flink.model.sink;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.flink.model.SinkConfig;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class StarRocksSinkConfig extends SinkConfig {

    @NotBlank
    String fenodes;
    @NotBlank
    String jdbcUrl;
    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    RdbmsTable rdbmsTable;

    @NotBlank
    String semantic = "exactly-once";
    @NotBlank
    String labelPrefix;

    @AssertFalse(message = "[labelPrefix] cannot be blank when semantic is 'exactly-once'.")
    public boolean isSinkOK() {
        return "exactly-once".equals(semantic) && StringUtils.isNotBlank(labelPrefix);
    }

    List<String> preQueries = new ArrayList<>();

    @Override
    public String getConnector() {
        return "starrocks";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("load-url", fenodes);
        defineOptions.put("jdbc-url", jdbcUrl);
        defineOptions.put("username", username);
        defineOptions.put("password", password);
        defineOptions.put("database-name", rdbmsTable.getDatabase());
        defineOptions.put("table-name", rdbmsTable.getTable());
        defineOptions.put("sink.semantic", semantic);
        defineOptions.put("sink.label-prefix", labelPrefix);
        return defineOptions;
    }
}