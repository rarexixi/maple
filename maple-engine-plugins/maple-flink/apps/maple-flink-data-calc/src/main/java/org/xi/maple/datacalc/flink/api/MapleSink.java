package org.xi.maple.datacalc.flink.api;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.Column;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.StructTableConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Map;

public abstract class MapleSink<T extends MapleSink.SinkConfig> extends MaplePlugin<T> implements TableDefine {

    public MapleSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = TableUtils.getTableDescriptor(config);
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }

    public void prepare() {
        Table sourceTable = tableEnv.sqlQuery(config.getSourceSql());
        List<Column> columns = sourceTable.getResolvedSchema().getColumns();
        if (columns.size() != config.getColumns().size()) {
            throw new ConfigRuntimeException("The number of columns in the source table and the target table is inconsistent");
        }
    }

    @Data
    public static abstract class SinkConfig extends StructTableConfig implements TableInsert {
        protected String sourceCatalogName;
        protected String sourceDatabaseName;
        protected String sourceTableName;

        protected String sourceSql;

        @AssertTrue(message = "[sourceSql, sourceTableName] cannot be blank at the same time.")
        public boolean isSourceOK() {
            return StringUtils.isNotBlank(sourceSql) || StringUtils.isNotBlank(sourceTableName);
        }

        public String getSourceSql() {
            if (StringUtils.isNotBlank(sourceSql)) {
                return sourceSql;
            }
            String sourceTable = TableUtils.getResultTable(sourceCatalogName, sourceDatabaseName, sourceTableName);
            return String.format("SELECT * FROM %s", sourceTable);
        }

        @Override
        public String getInsertSql() {
            return String.format("INSERT INTO %s %s", getResultTable(), getSourceSql());
        }
    }
}
