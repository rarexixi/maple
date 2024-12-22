package org.xi.maple.common.model.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GetSchemasReq extends JdbcBaseReq {
    // @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "databaseName 格式错误")
    String databaseName = "";

    @Override
    public Map<String, ?> getSqlParamMap() {
        Map<String, String> result = new HashMap<>();
        result.put("databaseName", databaseName);
        return result;
    }
}
