package org.xi.maple.mp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.mp.model.request.GetDatasourceSchemasReq;
import org.xi.maple.mp.model.request.GetDatasourceTableDetailReq;
import org.xi.maple.mp.model.request.GetDatasourceTablesReq;
import org.xi.maple.mp.service.JdbcService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping(JdbcController.BASE_URL)
@RestController
@Validated
public class JdbcController {

    public static final String BASE_URL = "/api/jdbc";
    private final JdbcService jdbcService;

    @Autowired
    public JdbcController(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @GetMapping("/{datasourceId}/dbs")
    public ResponseEntity<List<Map<String, Object>>> getDatabases(
            @PathVariable("datasourceId") Integer datasourceId) {
        return ResponseEntity.ok(jdbcService.getDatabases(datasourceId));
    }

    @GetMapping("/{datasourceId}/schemas")
    public ResponseEntity<List<Map<String, Object>>> getSchemas(
            @PathVariable("datasourceId") Integer datasourceId,
            @Validated GetDatasourceSchemasReq getSchemasReq) {
        return ResponseEntity.ok(jdbcService.getSchemas(datasourceId, getSchemasReq));
    }

    @GetMapping("/{datasourceId}/tables")
    public ResponseEntity<List<Map<String, Object>>> getTables(
            @PathVariable("datasourceId") Integer datasourceId,
            @Validated GetDatasourceTablesReq getTablesReq) {
        return ResponseEntity.ok(jdbcService.getTables(datasourceId, getTablesReq));
    }

    @GetMapping("/{datasourceId}/table")
    public ResponseEntity<TableDetailResp> getTableDetail(
            @PathVariable("datasourceId") Integer datasourceId,
            @Validated GetDatasourceTableDetailReq getTableDetailReq) {
        return ResponseEntity.ok(jdbcService.getTableDetail(datasourceId, getTableDetailReq));
    }

}
