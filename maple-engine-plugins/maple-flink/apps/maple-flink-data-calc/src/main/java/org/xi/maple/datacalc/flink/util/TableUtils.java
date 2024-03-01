package org.xi.maple.datacalc.flink.util;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.sql.parser.ddl.SqlTableColumn;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.xi.maple.datacalc.flink.model.CreateTableConfig;
import org.xi.maple.datacalc.flink.model.InsertTableConfig;
import org.xi.maple.datacalc.flink.model.definition.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.calcite.avatica.util.Quoting.BACK_TICK;

public class TableUtils {

    public static String getInsertSql(Map<String, List<String>> tableColumnsMap, InsertTableConfig insertTableConfig) {
        return "";
    }

    public static List<BaseColumn> get(String createSql) throws SqlParseException {
        SqlParser.Config sqlParserConfig = SqlParser.configBuilder()
                .setParserFactory(FlinkSqlParserImpl.FACTORY)
                .setQuoting(BACK_TICK)
                .setUnquotedCasing(Casing.TO_LOWER)
                .setQuotedCasing(Casing.UNCHANGED)
                .setConformance(FlinkSqlConformance.DEFAULT)
                .build();
        SqlParser parser = SqlParser.create(createSql, sqlParserConfig);

        SqlNode sqlNode = parser.parseStmt();
        if (sqlNode.getKind() != SqlKind.CREATE_TABLE) {
            throw new RuntimeException("createSql is invalid");
        }
        SqlCreateTable createTable = (SqlCreateTable) sqlNode;
        if (createTable.columnList == null) {
            return null;
        }
        List<BaseColumn> columns = new ArrayList<>(createTable.columnList.size());
        for (SqlNode column : createTable.columnList) {
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

    public static TableDescriptor getTableDescriptor(CreateTableConfig createTableConfig) {
        Schema.Builder schemaBuilder = Schema.newBuilder();
        for (BaseColumn column : createTableConfig.getColumns()) {
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
        if (createTableConfig.getPrimaryKey() != null) {
            PrimaryKeyDefinition pk = createTableConfig.getPrimaryKey();
            if (StringUtils.isBlank(pk.getName())) {
                schemaBuilder.primaryKey(pk.getColumns());
                schemaBuilder.primaryKey(pk.getColumns());
            } else {
                schemaBuilder.primaryKeyNamed(pk.getName(), pk.getColumns());
            }
        }
        if (createTableConfig.getWatermark() != null) {
            WatermarkDefinition watermark = createTableConfig.getWatermark();
            schemaBuilder.watermark(watermark.getColumnName(), watermark.getExpression());
        }

        TableDescriptor.Builder tableBuilder = TableDescriptor.forConnector(createTableConfig.getConnector()).schema(schemaBuilder.build());
        Map<String, String> defineOptions = createTableConfig.getDefineOptions();
        if (defineOptions != null) {
            defineOptions.forEach((key, value) -> {
                createTableConfig.getOptions().remove(key);
                tableBuilder.option(key, value);
            });
        }
        if (createTableConfig.getOptions() != null) {
            createTableConfig.getOptions().forEach(tableBuilder::option);
        }
        if (createTableConfig.getPartitionColumns() != null) {
            tableBuilder.partitionedBy(createTableConfig.getPartitionColumns());
        }
        tableBuilder.comment(createTableConfig.getComment());
        return tableBuilder.build();
    }
}
