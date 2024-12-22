package org.xi.maple.datacalc.flink.transform;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.datacalc.flink.api.MapleTransform;
import org.xi.maple.datacalc.flink.model.transform.SqlTransformConfig;

public class SqlTransform extends MapleTransform<SqlTransformConfig> {

    public SqlTransform(TableEnvironment tableEnv) {
        super(tableEnv);
    }

    @Override
    public void define() {
        tableEnv.createTemporaryView(config.getResultTable(), tableEnv.sqlQuery(config.getSql()));
    }
}
