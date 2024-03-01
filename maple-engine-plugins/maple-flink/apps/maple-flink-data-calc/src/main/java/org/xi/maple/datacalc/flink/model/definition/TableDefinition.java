package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Data
public abstract class TableDefinition extends MaplePluginConfig implements ResultTableConfig {

    String catalogName;
    String databaseName;
    @NotBlank
    String tableName;
    String comment;
    @NotEmpty
    @Valid
    List<BaseColumn> columns;
    PrimaryKeyDefinition primaryKey;

    WatermarkDefinition watermark;
    String[] partitionColumns;

    private Map<String, String> options = new LinkedHashMap<>();

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        if (options == null) {
            return;
        }
        this.options.putAll(options);
        this.options.remove("connector");
    }

    public abstract String getConnector();

    public abstract Map<String, String> getDefineOptions();

    public void setColumns(List<ColumnDefinition> columns) {
        for (ColumnDefinition cd : columns) {
            BaseColumn column;
            switch (cd.getColumnType()) {
                case "physical":
                    column = JsonUtils.convertValue(cd.getDefinition(), PhysicalColumn.class);
                    break;
                case "metadata":
                    column = JsonUtils.convertValue(cd.getDefinition(), MetadataColumn.class);
                    break;
                case "computed":
                    column = JsonUtils.convertValue(cd.getDefinition(), ComputedColumn.class);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown column type: " + cd.getColumnType());
            }
            this.columns.add(column);
        }
    }

    public TableDescriptor getTableDescriptor() {
        Schema.Builder schemaBuilder = Schema.newBuilder();
        List<Schema.UnresolvedColumn> unresolvedColumns = schemaBuilder.build().getColumns();
        for (BaseColumn column : this.columns) {
            if (column instanceof PhysicalColumn) {
                PhysicalColumn pc = (PhysicalColumn) column;
                schemaBuilder.column(pc.getName(), pc.getDataType()).withComment(pc.getComment());
            } else if (column instanceof MetadataColumn) {
                MetadataColumn mc = (MetadataColumn) column;
                schemaBuilder.columnByMetadata(mc.getName(), mc.getDataType(), mc.getMetadataKey(), mc.isVirtual()).withComment(mc.getComment());
            } else if (column instanceof ComputedColumn) {
                ComputedColumn cc = (ComputedColumn) column;
                schemaBuilder.columnByExpression(cc.getName(), cc.getExpression()).withComment(cc.getComment());
            }
        }
        if (this.primaryKey != null) {
            if (StringUtils.isBlank(this.primaryKey.getName())) {
                schemaBuilder.primaryKey(this.primaryKey.getColumns());
                schemaBuilder.primaryKey(this.primaryKey.getColumns());
            } else {
                schemaBuilder.primaryKeyNamed(this.primaryKey.getName(), this.primaryKey.getColumns());
            }
        }
        if (this.watermark != null) {
            schemaBuilder.watermark(this.watermark.getColumnName(), this.watermark.getExpression());
        }

        TableDescriptor.Builder tableBuilder = TableDescriptor.forConnector(getConnector()).schema(schemaBuilder.build());
        Map<String, String> defineOptions = getDefineOptions();
        if (defineOptions != null) {
            defineOptions.forEach((key, value) -> {
                this.options.remove(key);
                tableBuilder.option(key, value);
            });
        }
        if (this.options != null) {
            this.options.forEach(tableBuilder::option);
        }
        if (this.partitionColumns != null) {
            tableBuilder.partitionedBy(this.partitionColumns);
        }
        tableBuilder.comment(this.comment);
        return tableBuilder.build();
    }

    public String getResultTable() {
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
}
