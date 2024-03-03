package org.xi.maple.datacalc.flink.model;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.model.definition.*;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class CreateTableConfig extends MaplePluginConfig implements ResultTableConfig {

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

    public String getResultTable() {
        return TableUtils.getResultTable(catalogName, databaseName, tableName);
    }
}
