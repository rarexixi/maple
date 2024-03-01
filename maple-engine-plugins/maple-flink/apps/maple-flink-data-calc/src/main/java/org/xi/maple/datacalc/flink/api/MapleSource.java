package org.xi.maple.datacalc.flink.api;

import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.definition.TableDefinition;

import java.util.Map;

public abstract class MapleSource<T extends MapleSource.SourceConfig> extends MaplePlugin<T> implements TableDefine {

    public MapleSource(TableEnvironment tableEnv, Map<String, String> gv) {
        super(tableEnv, gv);
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = config.getTableDescriptor();
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }

    public static abstract class SourceConfig extends TableDefinition {
    }
}
