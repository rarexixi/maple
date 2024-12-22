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
public class TidbCdcSourceConfig extends SourceConfig {

    @NotBlank
    String pdAddresses;
    @NotNull
    RdbmsTable rdbmsTable;
    @Pattern(regexp = "initial|latest-offset")
    String scanStartupMode = "initial";

    @Override
    public String getConnector() {
        return "tidb-cdc";
    }

    @Override
    public Map<String, String> getDefineOptions() {
        Map<String, String> defineOptions = new LinkedHashMap<>();
        defineOptions.put("pd-addresses", pdAddresses);
        defineOptions.put("database-name", rdbmsTable.getDatabase());
        defineOptions.put("table-name", rdbmsTable.getTable());
        defineOptions.put("scan.startup.mode", scanStartupMode);
        return defineOptions;
    }
}