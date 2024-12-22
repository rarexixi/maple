package org.xi.maple.jdbc.api.service.impl;

import org.xi.maple.common.model.request.*;
import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.jdbc.api.configuration.MapleJdbcProperties;
import org.xi.maple.jdbc.api.service.JdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("databaseService")
public class JdbcServiceImpl implements JdbcService {

    private final Logger logger = LoggerFactory.getLogger(JdbcServiceImpl.class);

    final MapleJdbcProperties mapleJdbcProperties;

    @Autowired
    public JdbcServiceImpl(MapleJdbcProperties mapleJdbcProperties) {
        this.mapleJdbcProperties = mapleJdbcProperties;
    }

    @Override
    public List<Map<String, Object>> getDatabases(GetDatabasesReq getDatabasesReq) {
        return queryForList(mapleJdbcProperties.getGetDatabasesSql(), getDatabasesReq);
    }

    @Override
    public List<Map<String, Object>> getSchemas(GetSchemasReq getSchemasReq) {
        return queryForList(mapleJdbcProperties.getGetSchemasSql(), getSchemasReq);
    }

    @Override
    public List<Map<String, Object>> getTables(GetTablesReq getTablesReq) {
        return queryForList(mapleJdbcProperties.getGetTablesSql(), getTablesReq);
    }

    @Override
    public TableDetailResp getTableDetail(GetTableDetailReq getTableDetailReq) {
        TableDetailResp resp = new TableDetailResp();
        resp.setColumns(queryForList(mapleJdbcProperties.getGetTableColumnsSql(), getTableDetailReq));
        resp.setPkColumns(queryForList(mapleJdbcProperties.getGetTablePkColumnsSql(), getTableDetailReq));
        return resp;
    }

    private List<Map<String, Object>> queryForList(String sql, JdbcBaseReq jdbcBaseReq) {
        if (sql == null || sql.trim().isEmpty()) {
            return Collections.emptyList();
        }
        NamedParameterJdbcTemplate jdbcTemplate = getJdbcTemplate(jdbcBaseReq);
        return jdbcTemplate.queryForList(sql, jdbcBaseReq.getSqlParamMap());
    }


    private NamedParameterJdbcTemplate getJdbcTemplate(JdbcBaseReq jdbcReq) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcReq.getDriverClassName());
        dataSource.setUrl(jdbcReq.getUrl());
        dataSource.setUsername(jdbcReq.getUsername());
        dataSource.setPassword(jdbcReq.getPassword());
        return new NamedParameterJdbcTemplate(dataSource);
    }
}