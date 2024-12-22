package org.xi.maple.mp.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.xi.maple.common.constant.MapleServiceName;
import org.xi.maple.common.model.request.GetDatabasesReq;
import org.xi.maple.common.model.request.GetSchemasReq;
import org.xi.maple.common.model.request.GetTableDetailReq;
import org.xi.maple.common.model.request.GetTablesReq;
import org.xi.maple.common.model.response.TableDetailResp;
import org.xi.maple.service.configuration.RandomRouteLoadBalancerConfiguration;
import org.xi.maple.service.feign.MapleFeignHeadersInterceptor;

import java.util.List;
import java.util.Map;

@FeignClient(value = MapleServiceName.JDBC, configuration = MapleFeignHeadersInterceptor.class)
@LoadBalancerClient(name = MapleServiceName.JDBC, configuration = RandomRouteLoadBalancerConfiguration.class)
public interface JdbcClient {

    @PostMapping("/api/jdbc/dbs")
    List<Map<String, Object>> getDatabases(@RequestBody GetDatabasesReq getDatabasesReq);

    @PostMapping("/api/jdbc/schemas")
    List<Map<String, Object>> getSchemas(@RequestBody GetSchemasReq getSchemasReq);

    @PostMapping("/api/jdbc/tables")
    List<Map<String, Object>> getTables(@RequestBody GetTablesReq getTablesReq);

    @PostMapping("/api/jdbc/table")
    TableDetailResp getTableDetail(@RequestBody GetTableDetailReq getTableDetailReq);
}