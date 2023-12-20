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
    public List<CommandGeneratorModel> getCommandGenerator(EngineExecutionModel execution) {
        FlinkEngineExecution execConf;
        try {
            execConf = convert(execution);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();

        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-to-yarn.sh.ftl", "flink-to-yarn.sh", execConf));
        return commandGeneratorModels;
    }

    private FlinkEngineExecution convert(EngineExecutionModel execution) throws IOException {
        String executionConf = execution.getConfiguration();
        FlinkEngineExecution spark3EngineExecution = JsonUtils.parseObject(executionConf, FlinkEngineExecution.class);
        assert spark3EngineExecution != null;
        return spark3EngineExecution;
    }
}
