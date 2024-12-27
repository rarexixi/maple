package org.xi.maple.datacalc.flink.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TablePipeline;
import org.apache.flink.table.catalog.Column;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.SinkConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import java.util.List;

public abstract class MapleSink<T extends SinkConfig> extends MaplePlugin<T> implements TableDefine {

    final Table sourceTable;

    public MapleSink(TableEnvironment tableEnv) {
        super(tableEnv);
        sourceTable = StringUtils.isNotBlank(config.getSourceQuery())
                ? tableEnv.sqlQuery(config.getSourceQuery())
                : tableEnv.from(config.getSourceTable());
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = TableUtils.getTableDescriptor(config);
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }

    public TablePipeline getTablePipeline() {
        return sourceTable.insertInto(config.getResultTable());
    }

    @Override
    public void prepare() {
        List<Column> columns = sourceTable.getResolvedSchema().getColumns();
        int columnSize = config.getPhysicalColumns().size();
        if (!config.getMetadataColumns().isEmpty()) {
            columnSize += (int) config.getMetadataColumns().stream().filter(item -> !item.isVirtual()).count();
        }
        if (columns.size() != columnSize) {
            throw new ConfigRuntimeException("The number of columns in the source table and the target table is inconsistent");
        }
    }
}
