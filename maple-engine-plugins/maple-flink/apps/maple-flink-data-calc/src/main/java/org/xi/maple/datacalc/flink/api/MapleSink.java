package org.xi.maple.datacalc.flink.api;

import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.definition.TableDefinition;

import java.util.Map;

public abstract class MapleSink<T extends MapleSink.SinkConfig> extends MaplePlugin<T> implements TableDefine, TableInsert {

    public MapleSink(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = config.getTableDescriptor();
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }

    @Override
    public String getInsertSql() {
        return null;
    }

    public static abstract class SinkConfig extends TableDefinition {
    }
}
