<#macro str content>
  <#if (content?contains("\n"))>

  """
    |${content?replace("\n", "\n    |")}
    |""".stripMargin<#else>"${content}"</#if></#macro>
<#macro str_nowrap content>
  <#if (content?contains("\n"))>"""
    |${content?replace("\n", "\n    |")}
    |""".stripMargin<#else>"${content}"</#if></#macro>
import org.apache.spark.storage.StorageLevel
import org.xi.maple.util.HiveSinkUtils
import org.xi.maple.util.VariableUtils
import org.xi.maple.model.NamedDatasource
import scala.collection.JavaConverters.mapAsJavaMapConverter


def getDatasource(name: String): NamedDatasource = {
  // todo 待实现
  return null
}


def executeQueries(url: String, user: String, password: String, sqls: Seq[String], variables: Map[String, String, queryTimeout: Int = 0): Unit = {
  var conn: Option[java.sql.Connection] = None
  try {
    conn = Some(java.sql.DriverManager.getConnection(url, user, password))
    sqls.foreach(sql => {
      var statement: Option[java.sql.PreparedStatement] = None
      try {
        statement = Some(conn.get.prepareStatement(VariableUtils.replaceVariables(sql, variables.asJava)))
        if (queryTimeout > 0) {
          statement.get.setQueryTimeout(queryTimeout)
        }
        val rows = statement.get.executeUpdate()
        logger.info("{} rows affected", rows)
      } catch {
        case e: Exception => logger.error("Execute query failed. ", e)
      } finally {
        if (statement.isDefined) {
          statement.get.close()
        }
      }
    })
  } catch {
    case e: Exception => logger.error("Execute query failed. ", e)
  } finally {
    if (conn.isDefined) {
      conn.get.close()
    }
  }
}


def executeDatasourceQueries(datasource: NamedDatasource, sqls: Seq[String], variables: Map[String, String]): Unit = {
  executeQueries(datasource.getUrl, datasource.getUser, datasource.getPassword, sqls, variables)
}


val globalVariables = Map(
<#if variables??>
<#assign keys = variables?keys>
  <#list keys as key>
  "${key}" -> "${variables[key]?j_string}"<#sep>,</#sep>
  </#list>
</#if>
)

<#list plugins as plugin>
// ===================================== ${plugin.type} - plugin ${plugin?index + 1} =====================================
  <#assign config = plugin.config>
  <#assign prefix = plugin.type + (plugin?index + 1)>
  <#if (plugin.type == "source")>
    <#if (plugin.name == "jdbc")>
      <#include "/plugins/source/source_jdbc.ftl">
    <#elseif (plugin.name == "managed_jdbc")>
      <#include "/plugins/source/source_managed_jdbc.ftl">
    <#elseif (plugin.name == "file")>
      <#include "/plugins/source/source_file.ftl">
    <#else>
    </#if>
  <#elseif (plugin.type == "transformation")>
    <#if (plugin.name == "sql")>
      <#include "/plugins/transform/transform_sql.ftl">
    <#else>
    </#if>
  <#elseif (plugin.type == "sink")>
    <#if (plugin.name == "jdbc")>
      <#include "/plugins/sink/sink_jdbc.ftl">
    <#elseif (plugin.name == "managed_jdbc")>
      <#include "/plugins/sink/sink_managed_jdbc.ftl">
    <#elseif (plugin.name == "file")>
      <#include "/plugins/sink/sink_file.ftl">
    <#elseif (plugin.name == "hive")>
      <#include "/plugins/sink/sink_hive.ftl">
    <#else>
    </#if>
  </#if>
</#list>