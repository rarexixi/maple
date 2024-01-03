package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.ExecFtlModel;
import org.xi.maple.builder.model.Spark3EngineExecution;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3YarnConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3EngineExecution> execConf = convert(execution);

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "spark-yarn-submit.sh.ftl", "spark3-yarn-submit.sh", execConf));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3EngineExecution> execConf = convert(execution);

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-stop.sh.ftl", "flink-yarn-stop.sh", execConf));
        return commandGeneratorModels;
    }

    private ExecFtlModel<Spark3EngineExecution> convert(EngineExecutionModel execution) {
        String executionConf = execution.getConfiguration();
        ExecFtlModel<Spark3EngineExecution> execModel = new ExecFtlModel<>();
        execModel.setExecId(execution.getExecId());
        execModel.setExecFile(execution.getExecFile());
        execModel.setFromApp(execution.getFromApp());
        execModel.setJobId(execution.getJobId());
        execModel.setBizId(execution.getBizId());
        execModel.setExecUniqId(execution.getExecUniqId());
        execModel.setExecName(execution.getExecName());
        execModel.setResourceGroup(execution.getResourceGroup());
        execModel.setGroup(execution.getGroup());
        execModel.setUser(execution.getUser());
        execModel.setEngine(execution.getEngine());
        Spark3EngineExecution jobConf = JsonUtils.parseObject(executionConf, Spark3EngineExecution.class, null);
        // todo 根据 runType 设置 runConf
        if (jobConf != null) {
            jobConf.setQueue(execution.getResourceGroup());
            if (execution.getEngine().getConfs() != null) {
                jobConf.setConf(MapUtils.mergeMap(execution.getEngine().getConfs(), jobConf.getConf()));
            }
        }
        execModel.setJob(jobConf);
        return execModel;
    }
}
