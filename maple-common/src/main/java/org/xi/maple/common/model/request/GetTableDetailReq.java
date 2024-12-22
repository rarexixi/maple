package org.xi.maple.common.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
public class GetTableDetailReq extends JdbcBaseReq {
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "databaseName 格式错误")
    String databaseName = "";
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "schemaName 格式错误")
    String schemaName = "";
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "tableName 格式错误")
    String tableName = "";

    @Override
    public Map<String, ?> getSqlParamMap() {
        Map<String, String> result = new HashMap<>();
        result.put("databaseName", databaseName);
        result.put("schemaName", schemaName);
        result.put("tableName", tableName);
        return result;
    }
}
