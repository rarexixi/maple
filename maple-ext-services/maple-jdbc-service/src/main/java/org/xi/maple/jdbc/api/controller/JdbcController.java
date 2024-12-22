package org.xi.maple.jdbc.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.xi.maple.common.model.request.GetDatabasesReq;
import org.xi.maple.common.model.request.GetSchemasReq;
import org.xi.maple.common.model.request.GetTableDetailReq;
import org.xi.maple.common.model.request.GetTablesReq;
import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.jdbc.api.service.JdbcService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api/jdbc")
@RestController
public class JdbcController {

    final JdbcService jdbcService;

    public JdbcController(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @PostMapping("/dbs")
    public ResponseEntity<List<Map<String, Object>>> getDatabases(@Validated @RequestBody GetDatabasesReq getDatabasesReq) {
        return ResponseEntity.ok(jdbcService.getDatabases(getDatabasesReq));
    }

    @PostMapping("/schemas")
    public ResponseEntity<List<Map<String, Object>>> getSchemas(@Validated @RequestBody GetSchemasReq getSchemasReq) {
        return ResponseEntity.ok(jdbcService.getSchemas(getSchemasReq));
    }

    @PostMapping("/tables")
    public ResponseEntity<List<Map<String, Object>>> getTables(@Validated @RequestBody GetTablesReq getTablesReq) {
        return ResponseEntity.ok(jdbcService.getTables(getTablesReq));
    }

    @PostMapping("/table")
    public ResponseEntity<TableDetailResp> getTableDetail(@Validated @RequestBody GetTableDetailReq getTableDetailReq) {
        return ResponseEntity.ok(jdbcService.getTableDetail(getTableDetailReq));
    }
}
