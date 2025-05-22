package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;

import java.util.Arrays;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.K8s)
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3K8sConvertor extends Spark3Convertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3RunConf.K8s, Spark3ExecConf.ExecConf> execConf = convert(execution);
        String resultFileName = "spark3-k8s-submit.yaml";
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    @Override
    public List<CommandGeneratorModel> getOperateCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3RunConf.K8s, Spark3ExecConf.ExecConf> execConf = convert(execution);
        String resultFileName = String.format("spark3-k8s-%s.yaml", execution.getAction());
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    private ExecFtlModel<Spark3RunConf.K8s, Spark3ExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<Spark3RunConf.K8s, Spark3ExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution, Spark3RunConf.K8s.class));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }
}
