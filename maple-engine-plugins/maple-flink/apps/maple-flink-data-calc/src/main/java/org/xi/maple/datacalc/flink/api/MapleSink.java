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

    public MapleSink(TableEnvironment tableEnv) {
        super(tableEnv);
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = TableUtils.getTableDescriptor(config);
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }

    public TablePipeline getTablePipeline() {
        if (StringUtils.isNotBlank(config.getSourceSql())) {
            return tableEnv.sqlQuery(config.getSourceSql()).insertInto(config.getResultTable());
        } else {
            return tableEnv.from(config.getSourceTableName()).insertInto(config.getResultTable());
        }
    }

    public void prepare() {
        Table sourceTable = tableEnv.sqlQuery(config.getSourceSql());
        List<Column> columns = sourceTable.getResolvedSchema().getColumns();
        if (columns.size() != config.getColumns().size()) {
            throw new ConfigRuntimeException("The number of columns in the source table and the target table is inconsistent");
        }
    }
}
