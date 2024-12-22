package org.xi.maple.datacalc.flink.api;

import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.model.SourceConfig;
import org.xi.maple.datacalc.flink.util.TableUtils;

public abstract class MapleSource<T extends SourceConfig> extends MaplePlugin<T> implements TableDefine {

    public MapleSource(TableEnvironment tableEnv) {
        super(tableEnv);
    }

    @Override
    public void define() {
        TableDescriptor tableDescriptor = TableUtils.getTableDescriptor(config);
        tableEnv.createTemporaryTable(config.getResultTable(), tableDescriptor);
    }
}
