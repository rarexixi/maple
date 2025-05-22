package org.xi.maple.builder.convertor;

import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.FlinkExecConf;
import org.xi.maple.builder.model.FlinkRunConf;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapUtils;

public abstract class FlinkConvertor implements MapleConvertor {
    protected FlinkExecConf.ExecConf getExecConf(EngineExecutionModel execution) {
        String executionConf = execution.getExecConf();
        FlinkExecConf.ExecConf execConf;
        if ("flink-data-calc".equalsIgnoreCase(execution.getJobType())) {
            execConf =JsonUtils.parseObject(executionConf, FlinkExecConf.DataCalc.class, null);
        } else if ("flink-sql".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, FlinkExecConf.Sql.class, null);
        } else if ("flink-jar".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, FlinkExecConf.Jar.class, null);
        } else if ("flink-py".equalsIgnoreCase(execution.getJobType())) {
            execConf = JsonUtils.parseObject(executionConf, FlinkExecConf.Py.class, null);
        } else {
            throw new MapleException("unknown job type: " + execution.getJobType());
        }
        if (execConf == null) {
            throw new MapleException("ExecConf has errors.");
        }
        return execConf;
    }

    protected <T extends FlinkRunConf.RunConf> T getRunConf(EngineExecutionModel execution, Class<T> clazz) {
        T runConf = JsonUtils.parseObject(execution.getRunConf(), clazz, null);
        if (runConf == null) {
            throw new MapleException("RunConf has errors.");
        }
        if (execution.getEngine().getConfs() != null) {
            runConf.setConf(MapUtils.mergeMap(execution.getEngine().getConfs(), runConf.getConf()));
        } else {
            runConf.setConf(runConf.getConf());
        }
        return runConf;
    }
}
