package org.xi.maple.builder.convertor;

import org.xi.maple.builder.model.*;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.JsonUtils;

public abstract class Spark3Convertor implements MapleConvertor {

    protected Spark3ExecConf.ExecConf getExecConf(EngineExecutionModel execution) {
        String executionConf = execution.getExecConf();
        Spark3ExecConf.ExecConf execConf;
        if ("spark3-data-calc".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, Spark3ExecConf.DataCalc.class, null);
        } else if ("spark3-sql".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, Spark3ExecConf.Sql.class, null);
        } else if ("spark3-jar".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, Spark3ExecConf.Jar.class, null);
        } else if ("spark3-py".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, Spark3ExecConf.Py.class, null);
        } else {
            throw new MapleException("unknown job type: " + execution.getJobType());
        }
        if (execConf == null) {
            throw new MapleException("ExecConf has errors.");
        }
        return execConf;
    }
}
