package org.xi.maple.mp.service;

import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.mp.model.request.GetDatasourceSchemasReq;
import org.xi.maple.mp.model.request.GetDatasourceTableDetailReq;
import org.xi.maple.mp.model.request.GetDatasourceTablesReq;

import java.util.List;
import java.util.Map;

public interface JdbcService {
    List<Map<String, Object>> getDatabases(Integer datasourceId);
    List<Map<String, Object>> getSchemas(Integer datasourceId, GetDatasourceSchemasReq getSchemasReq);
    List<Map<String, Object>> getTables(Integer datasourceId, GetDatasourceTablesReq getTablesReq);
    TableDetailResp getTableDetail(Integer datasourceId, GetDatasourceTableDetailReq getTableDetailReq);
}
