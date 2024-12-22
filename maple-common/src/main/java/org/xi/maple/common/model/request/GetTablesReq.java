package org.xi.maple.common.model.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GetTablesReq extends JdbcBaseReq {
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "databaseName 格式错误")
    String databaseName = "";
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "schemaName 格式错误")
    String schemaName = "";

    @Override
    public Map<String, ?> getSqlParamMap() {
        Map<String, String> result = new HashMap<>();
        result.put("databaseName", databaseName);
        result.put("schemaName", schemaName);
        return result;
    }
}
