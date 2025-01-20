package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkYarnConvertor extends FlinkConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel("flink-yarn-submit.sh", "flink-yarn-submit.sh.ftl", execConf, true));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel("flink-yarn-stop.sh", "flink-yarn-stop.sh.ftl", execConf, true));
        return commandGeneratorModels;
    }

    private ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }

    FlinkRunConf.Yarn getRunConf(EngineExecutionModel execution) {
        FlinkRunConf.Yarn runConf = JsonUtils.parseObject(execution.getRunConf(), FlinkRunConf.Yarn.class, null);
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
