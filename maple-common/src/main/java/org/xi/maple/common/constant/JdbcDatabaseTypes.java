package org.xi.maple.common.constant;

import lombok.Getter;
import org.xi.maple.common.util.FreemarkerUtils;

import java.util.Map;

import static org.xi.maple.common.constant.JdbcUrlFtlConstants.*;

@Getter
public enum JdbcDatabaseTypes {

    MYSQL("mysql", "MySQL", "mysql", false, true, MYSQL_JDBC_URL_FTL),
    POSTGRESQL("postgresql", "PostgreSQL", "postgresql", true, false, POSTGRESQL_JDBC_URL_FTL),
    ORACLE("oracle", "Oracle", "oracle", true, false, ORACLE_JDBC_URL_FTL),
    SQLSERVER("sqlserver", "SQL Server", "sqlserver", true, true, SQLSERVER_JDBC_URL_FTL),
    DB2("db2", "DB2", "db2", true, true, DB2_JDBC_URL_FTL),
    TIDB("tidb", "TiDB", "mysql", false, true, TIDB_JDBC_URL_FTL),
    DORIS("doris", "Doris", "doris", false, true, DORIS_JDBC_URL_FTL),
    STARROCKS("starrocks", "StarRocks", "mysql", false, true, STARROCKS_JDBC_URL_FTL),
    OCEANBASE("oceanbase", "OceanBase", "oceanbase", false, true, OCEANBASE_JDBC_URL_FTL),
    CLICKHOUSE("clickhouse", "ClickHouse", "clickhouse", false, true, CLICKHOUSE_JDBC_URL_FTL);

    final String value;
    final String name;
    final String usedAs;
    final boolean hasSchema;
    final boolean selectCrossDatabase;
    final String jdbcUrlFtl;

    JdbcDatabaseTypes(String value, String name, String usedAs, boolean hasSchema, boolean selectCrossDatabase, String jdbcUrlFtl) {
        this.value = value;
        this.name = name;
        this.usedAs = usedAs;
        this.hasSchema = hasSchema;
        this.selectCrossDatabase = selectCrossDatabase;
        this.jdbcUrlFtl = jdbcUrlFtl;
    }

    public static JdbcDatabaseTypes fromValue(String value) {
        for (JdbcDatabaseTypes type : JdbcDatabaseTypes.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    public String getJdbcUrl(Map<String, Object> databaseConf) {
        return FreemarkerUtils.process(jdbcUrlFtl, databaseConf);
    }
}
