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

@ClusterCategory(ClusterCategoryConstants.K8s)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkK8sConvertor extends FlinkConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel("flink-k8s-submit.yaml", "flink-k8s-submit.yaml.ftl", execConf, true));
        return commandGeneratorModels;
    }

    @Override
    public List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execConf = convert(execution);
        if (execConf == null) {
            return null;
        }

        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel("flink-k8s-stop.yaml", "flink-k8s-stop.yaml.ftl", execConf, true));
        return commandGeneratorModels;
    }

    private ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }
    
    FlinkRunConf.K8s getRunConf(EngineExecutionModel execution) {
        FlinkRunConf.K8s runConf = JsonUtils.parseObject(execution.getRunConf(), FlinkRunConf.K8s.class, null);
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
