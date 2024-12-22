package org.xi.maple.mp.model.request;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class GetDatasourceTableDetailReq {
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "databaseName 格式错误")
    String databaseName = "";
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "schemaName 格式错误")
    String schemaName = "";
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "tableName 格式错误")
    String tableName = "";
}
