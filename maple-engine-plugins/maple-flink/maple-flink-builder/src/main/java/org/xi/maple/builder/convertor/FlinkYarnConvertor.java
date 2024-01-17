package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.ExecFtlModel;
import org.xi.maple.builder.model.FlinkYarnDataModel;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkYarnConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkYarnDataModel> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-submit.sh.ftl", "flink-yarn-submit.sh", execConf));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkYarnDataModel> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "flink-yarn-stop.sh.ftl", "flink-yarn-stop.sh", execConf));
        return commandGeneratorModels;
    }

    private ExecFtlModel<FlinkYarnDataModel> convert(EngineExecutionModel execution) {
        ExecFtlModel<FlinkYarnDataModel> execModel = new ExecFtlModel<>(execution);

        String executionConf = execution.getConfiguration();
        FlinkYarnDataModel jobConf = JsonUtils.parseObject(executionConf, FlinkYarnDataModel.class, null);
        if (jobConf != null) {
        }
        execModel.setJob(jobConf);

        return execModel;
    }
}
