package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.FlinkEngineExecution;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkYarnConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        FlinkEngineExecution execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-submit.sh.ftl", "flink-yarn-submit.sh", execConf));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        FlinkEngineExecution execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-stop.sh.ftl", "flink-yarn-stop.sh", execConf));
        return commandGeneratorModels;
    }

    private FlinkEngineExecution convert(EngineExecutionModel execution) {
        String executionConf = execution.getConfiguration();
        FlinkEngineExecution flinkEngineExecution = JsonUtils.parseObject(executionConf, FlinkEngineExecution.class, null);
        if (flinkEngineExecution != null) {
        }
        return flinkEngineExecution;
    }
}
