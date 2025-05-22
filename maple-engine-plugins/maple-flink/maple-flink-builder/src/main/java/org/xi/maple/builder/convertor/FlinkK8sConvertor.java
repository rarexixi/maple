package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.K8s)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkK8sConvertor extends FlinkConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execConf = convert(execution);
        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        String resultFileName = "flink-k8s-submit.yaml";
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    @Override
    public List<CommandGeneratorModel> getOperateCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execConf = convert(execution);
        String resultFileName = String.format("flink-k8s-%s.yaml", execution.getAction());
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    private ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.K8s, FlinkExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution, FlinkRunConf.K8s.class));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }
}
