package org.xi.maple.jdbc.api.service;

import org.xi.maple.common.model.request.GetDatabasesReq;
import org.xi.maple.common.model.request.GetSchemasReq;
import org.xi.maple.common.model.request.GetTableDetailReq;
import org.xi.maple.common.model.request.GetTablesReq;
import org.xi.maple.common.model.response.TableDetailResp;

import java.util.List;
import java.util.Map;

public interface JdbcService {

    List<Map<String, Object>> getDatabases(GetDatabasesReq getDatabasesReq);

    List<Map<String, Object>> getSchemas(GetSchemasReq getSchemasReq);

    List<Map<String, Object>> getTables(GetTablesReq getTablesReq);

    TableDetailResp getTableDetail(GetTableDetailReq getTableDetailReq);
}
