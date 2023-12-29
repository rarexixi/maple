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

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.K8s)
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3K8sConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3EngineExecution> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "spark-yarn-submit.sh.ftl", "spark3-yarn-submit.sh", execConf));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3EngineExecution> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-stop.sh.ftl", "flink-yarn-stop.sh", execConf));
        return commandGeneratorModels;
    }

    private ExecFtlModel<Spark3EngineExecution> convert(EngineExecutionModel execution) {
        String executionConf = execution.getConfiguration();
        ExecFtlModel<Spark3EngineExecution> execModel = new ExecFtlModel<>();
        Spark3EngineExecution spark3EngineExecution = JsonUtils.parseObject(executionConf, Spark3EngineExecution.class, null);
        // todo 根据 runType 设置 runConf
        if (spark3EngineExecution != null) {
            spark3EngineExecution.setQueue(execution.getResourceGroup());
        }
        return execModel;
    }
}
