package org.xi.maple.datacalc.flink.util;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.sql.parser.ddl.SqlCreateTable;
import org.apache.flink.sql.parser.ddl.SqlTableColumn;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.StructTableConfig;
import org.xi.maple.datacalc.flink.model.definition.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.calcite.avatica.util.Quoting.BACK_TICK;

public class TableUtils {

    public static String getResultTable(String catalogName, String databaseName, String tableName) {
        StringBuilder tableNameBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(catalogName)) {
            tableNameBuilder.append(catalogName).append(".");
        }

        if (StringUtils.isNotBlank(databaseName)) {
            tableNameBuilder.append(databaseName).append(".");
        }

        tableNameBuilder.append(tableName);
        return tableNameBuilder.toString();
    }

    public static List<BaseColumn> getColumns(String createSql) {
        SqlCreateTable createTable = getSqlCreateTable(createSql);
        SqlNodeList columnList = createTable.getColumnList();
        if (columnList == null) {
            return null;
        }
        List<BaseColumn> columns = new ArrayList<>(columnList.size());
        for (SqlNode column : columnList) {
            if (column instanceof SqlTableColumn.SqlMetadataColumn) {
                SqlTableColumn.SqlMetadataColumn mc = (SqlTableColumn.SqlMetadataColumn) column;
                MetadataColumn metadataColumn = new MetadataColumn();
                metadataColumn.setName(mc.getName().toString());
                metadataColumn.setDataType(mc.getType().toString());
                columns.add(metadataColumn);
            } else if (column instanceof SqlTableColumn.SqlComputedColumn) {
                SqlTableColumn.SqlComputedColumn cc = (SqlTableColumn.SqlComputedColumn) column;
                ComputedColumn computedColumn = new ComputedColumn();
                computedColumn.setName(cc.getName().toString());
            } else if (column instanceof SqlTableColumn.SqlRegularColumn) {
                SqlTableColumn.SqlRegularColumn re =  (SqlTableColumn.SqlRegularColumn) column;
            }
        }
        return columns;
    }

    public static String getTableName(String createSql) {
        SqlCreateTable createTable = getSqlCreateTable(createSql);
        return createTable.getTableName().toString();
    }

    private static SqlCreateTable getSqlCreateTable(String createSql) {
        SqlParser.Config sqlParserConfig = getSqlParserConfig();
        SqlParser parser = SqlParser.create(createSql, sqlParserConfig);
        try {
            SqlNode sqlNode = parser.parseStmt();
            if (sqlNode.getKind() != SqlKind.CREATE_TABLE) {
                throw new RuntimeException("createSql is invalid");
            }
            return (SqlCreateTable) sqlNode;
        } catch (SqlParseException e) {
            throw new ConfigRuntimeException(e);
        }
    }

    private static SqlParser.Config getSqlParserConfig() {
        return SqlParser.configBuilder()
                .setParserFactory(FlinkSqlParserImpl.FACTORY)
                .setQuoting(BACK_TICK)
                .setUnquotedCasing(Casing.TO_LOWER)
                .setQuotedCasing(Casing.UNCHANGED)
                .setConformance(FlinkSqlConformance.DEFAULT)
                .build();
    }

    public static TableDescriptor getTableDescriptor(StructTableConfig structTableConfig) {
        Schema.Builder schemaBuilder = Schema.newBuilder();
        for (BaseColumn column : structTableConfig.getColumns()) {
            if (column instanceof PhysicalColumn) {
                PhysicalColumn pc = (PhysicalColumn) column;
                // new Schema.UnresolvedPhysicalColumn(pc.getName(), DataTypes.of(pc.getDataType()), pc.getComment());
                schemaBuilder.column(pc.getName(), pc.getDataType()).withComment(pc.getComment());
            } else if (column instanceof MetadataColumn) {
                MetadataColumn mc = (MetadataColumn) column;
                // new Schema.UnresolvedMetadataColumn(mc.getName(), DataTypes.of(mc.getDataType()), mc.getMetadataKey(), mc.isVirtual(), mc.getComment());
                schemaBuilder.columnByMetadata(mc.getName(), mc.getDataType(), mc.getMetadataKey(), mc.isVirtual()).withComment(mc.getComment());
            } else if (column instanceof ComputedColumn) {
                ComputedColumn cc = (ComputedColumn) column;
                // new Schema.UnresolvedComputedColumn(cc.getName(), new SqlCallExpression(cc.getExpression()), cc.getComment());
                schemaBuilder.columnByExpression(cc.getName(), cc.getExpression()).withComment(cc.getComment());
            }
        }
        if (structTableConfig.getPrimaryKey() != null) {
            PrimaryKeyDefinition pk = structTableConfig.getPrimaryKey();
            if (StringUtils.isBlank(pk.getName())) {
                schemaBuilder.primaryKey(pk.getColumns());
                schemaBuilder.primaryKey(pk.getColumns());
            } else {
                schemaBuilder.primaryKeyNamed(pk.getName(), pk.getColumns());
            }
        }
        if (structTableConfig.getWatermark() != null) {
            WatermarkDefinition watermark = structTableConfig.getWatermark();
            schemaBuilder.watermark(watermark.getColumnName(), watermark.getExpression());
        }

        TableDescriptor.Builder tableBuilder = TableDescriptor.forConnector(structTableConfig.getConnector()).schema(schemaBuilder.build());
        Map<String, String> defineOptions = structTableConfig.getDefineOptions();
        if (defineOptions != null) {
            defineOptions.forEach((key, value) -> {
                structTableConfig.getOptions().remove(key);
                tableBuilder.option(key, value);
            });
        }
        if (structTableConfig.getOptions() != null) {
            structTableConfig.getOptions().forEach(tableBuilder::option);
        }
        if (structTableConfig.getPartitionColumns() != null) {
            tableBuilder.partitionedBy(structTableConfig.getPartitionColumns());
        }
        tableBuilder.comment(structTableConfig.getComment());
        return tableBuilder.build();
    }
}
