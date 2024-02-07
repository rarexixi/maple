/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xi.maple.datacalc.service;

import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.exception.DatasourceNotConfigException;
import org.xi.maple.datacalc.model.NamedDatasource;
import org.xi.maple.datacalc.service.strategy.DataSourceStrategy;
import org.xi.maple.datacalc.service.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DataSourceContext {

    private static final Map<String, DataSourceStrategy> dsStrategyMap = new HashMap<>();

    static {
        // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
        dsStrategyMap.put("mysql", new MySqlStrategy());
        // https://docs.pingcap.com/tidb/dev/dev-guide-connect-to-tidb
        dsStrategyMap.put("tidb", new TiDBStrategy());
        dsStrategyMap.put("doris", new DorisStrategy());
        // https://jdbc.postgresql.org/documentation/use/
        dsStrategyMap.put("postgresql", new PostgreSqlStrategy());
        // https://github.com/ClickHouse/clickhouse-jdbc/tree/master/clickhouse-jdbc
        dsStrategyMap.put("clickhouse", new ClickHouseStrategy());
        // https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/oracle/jdbc/OracleDriver.html
        dsStrategyMap.put("oracle", new OracleStrategy());
        // https://learn.microsoft.com/zh-cn/sql/connect/jdbc/building-the-connection-url?redirectedfrom=MSDN&view=sql-server-ver16
        dsStrategyMap.put("sqlserver", new SqlServerStrategy());
        // https://www.ibm.com/docs/en/db2/11.5?topic=cdsudidsdjs-url-format-data-server-driver-jdbc-sqlj-type-4-connectivity
        dsStrategyMap.put("db2", new DB2Strategy());
    }

    private final DataSourceStrategy dataSourceStrategy;

    private final String datasourceType;
    private final String configJson;

    public DataSourceContext(String datasourceType, String configJson) {
        this.datasourceType = datasourceType;
        this.configJson = configJson;
        this.dataSourceStrategy = getDataSourceStrategy(datasourceType);
    }

    private DataSourceStrategy getDataSourceStrategy(String databaseType) {
        if (dsStrategyMap.containsKey(databaseType)) {
            return dsStrategyMap.get(databaseType);
        } else {
            throw new DatasourceNotConfigException("Datasource type [" + databaseType + "] not config");
        }
    }

    public NamedDatasource getDataSource() {
        Map<String, Object> connectParams = JsonUtils.parseObject(configJson, Map.class, new HashMap<String, String>());
        Map<String, String> params = new HashMap<>(0);
        if (connectParams != null) {
            params = connectParams.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
        }
        String defaultDriver = dataSourceStrategy.defaultDriver();
        String paramsJson = params.getOrDefault("params", "{}");
        String address = params.getOrDefault("address", "");

        NamedDatasource datasource = new NamedDatasource();
        datasource.setDatasourceType(datasourceType);
        datasource.setDriver(params.getOrDefault("driverClassName", defaultDriver));
        datasource.setUrl(dataSourceStrategy.getJdbcUrl(address, params, paramsJson));
        datasource.setUser(params.getOrDefault("username", ""));
        datasource.setPassword(params.getOrDefault("password", ""));
        return datasource;
    }
}
