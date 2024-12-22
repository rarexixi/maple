package org.xi.maple.common.constant;

public interface JdbcUrlFtlConstants {

    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    String MYSQL_JDBC_URL_FTL = "jdbc:mysql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    // https://jdbc.postgresql.org/documentation/use/
    String POSTGRESQL_JDBC_URL_FTL = "jdbc:postgresql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    // https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/oracle/jdbc/OracleDriver.html
    String ORACLE_JDBC_URL_FTL = "jdbc:oracle:thin:@//<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (serviceName??) && ((serviceName?trim) != '')>${serviceName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    // https://learn.microsoft.com/zh-cn/sql/connect/jdbc/building-the-connection-url?redirectedfrom=MSDN&view=sql-server-ver16
    String SQLSERVER_JDBC_URL_FTL = "jdbc:sqlserver://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>;databaseName=${databaseName}</#if><#if (params??) && ((params?size) > 0)>;<#list params as key, value>${key}=${value}<#if key?has_next>;</#if></#list></#if>";
    // https://www.ibm.com/docs/en/db2/11.5?topic=cdsudidsdjs-url-format-data-server-driver-jdbc-sqlj-type-4-connectivity
    String DB2_JDBC_URL_FTL = "jdbc:db2://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>:<#list params as key, value>${key}=${value}<#if key?has_next>;</#if></#list></#if>";
    // https://docs.pingcap.com/tidb/dev/dev-guide-connect-to-tidb
    String TIDB_JDBC_URL_FTL = "jdbc:mysql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    String DORIS_JDBC_URL_FTL = "jdbc:mysql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    String STARROCKS_JDBC_URL_FTL = "jdbc:mysql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    String OCEANBASE_JDBC_URL_FTL = "jdbc:mysql://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
    // https://github.com/ClickHouse/clickhouse-jdbc/tree/master/clickhouse-jdbc
    String CLICKHOUSE_JDBC_URL_FTL = "jdbc:clickhouse://<#if (address??) && ((address?trim) != '')>${address}<#else>${host}:${port}</#if>/<#if (databaseName??) && ((databaseName?trim) != '')>${databaseName}</#if><#if (params??) && ((params?size) > 0)>?<#list params as key, value>${key}=${value}<#if key?has_next>&</#if></#list></#if>";
}
