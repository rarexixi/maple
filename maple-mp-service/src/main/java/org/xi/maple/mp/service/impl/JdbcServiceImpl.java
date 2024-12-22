package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.JdbcDatabaseTypes;
import org.xi.maple.common.exception.MapleValidException;
import org.xi.maple.common.model.request.*;
import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.mp.client.JdbcClient;
import org.xi.maple.mp.model.request.GetDatasourceSchemasReq;
import org.xi.maple.mp.model.request.GetDatasourceTableDetailReq;
import org.xi.maple.mp.model.request.GetDatasourceTablesReq;
import org.springframework.stereotype.Service;
import org.xi.maple.mp.persistence.entity.DatasourceEntity;
import org.xi.maple.mp.persistence.mapper.DatasourceMapper;
import org.xi.maple.mp.service.JdbcService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class JdbcServiceImpl implements JdbcService {

    final JdbcClient jdbcClient;
    final DatasourceMapper datasourceMapper;

    public JdbcServiceImpl(JdbcClient jdbcClient, DatasourceMapper datasourceMapper) {
        this.jdbcClient = jdbcClient;
        this.datasourceMapper = datasourceMapper;
    }

    @Override
    public List<Map<String, Object>> getDatabases(Integer datasourceId) {
        DatasourceEntity datasource = datasourceMapper.getById(datasourceId);
        GetDatabasesReq req = new GetDatabasesReq();
        setJdbcConf(req, datasource);
        return jdbcClient.getDatabases(req);
    }

    @Override
    public List<Map<String, Object>> getSchemas(Integer datasourceId, GetDatasourceSchemasReq getSchemasReq) {
        DatasourceEntity datasource = datasourceMapper.getById(datasourceId);
        GetSchemasReq req = new GetSchemasReq();
        setJdbcConf(req, datasource);
        req.setDatabaseName(getSchemasReq.getDatabaseName());
        return jdbcClient.getSchemas(req);
    }

    @Override
    public List<Map<String, Object>> getTables(Integer datasourceId, GetDatasourceTablesReq getTablesReq) {
        DatasourceEntity datasource = datasourceMapper.getById(datasourceId);
        GetTablesReq req = new GetTablesReq();
        setJdbcConf(req, datasource);
        req.setDatabaseName(getTablesReq.getDatabaseName());
        req.setSchemaName(getTablesReq.getSchemaName());
        return jdbcClient.getTables(req);
    }

    @Override
    public TableDetailResp getTableDetail(Integer datasourceId, GetDatasourceTableDetailReq getTableDetailReq) {
        DatasourceEntity datasource = datasourceMapper.getById(datasourceId);
        GetTableDetailReq req = new GetTableDetailReq();
        setJdbcConf(req, datasource);
        req.setDatabaseName(getTableDetailReq.getDatabaseName());
        req.setSchemaName(getTableDetailReq.getSchemaName());
        req.setTableName(getTableDetailReq.getTableName());
        return jdbcClient.getTableDetail(req);
    }

    private <T extends JdbcBaseReq> void setJdbcConf(T jdbcBaseReq, DatasourceEntity datasource) {
        JdbcDatabaseTypes jdbcDatabaseType = JdbcDatabaseTypes.fromValue(datasource.getDatasourceType());
        if (jdbcDatabaseType == null) {
            // todo
            throw new MapleValidException("Unknown database type");
        }
        Map<String, Object> map = (Map<String, Object>) JsonUtils.parseObject(datasource.getDatasourceConf(), Map.class, Collections.emptyMap());
        String jdbcUrl = jdbcDatabaseType.getJdbcUrl(map);
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String driverClassName = map.get("driverClassName").toString();
        jdbcBaseReq.setUrl(jdbcUrl);
        jdbcBaseReq.setUsername(username);
        jdbcBaseReq.setPassword(password);
        jdbcBaseReq.setDriverClassName(driverClassName);
    }
}
