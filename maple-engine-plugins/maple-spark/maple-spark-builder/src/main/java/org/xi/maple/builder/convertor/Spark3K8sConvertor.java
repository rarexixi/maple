package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.K8s)
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3K8sConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3K8sDataModel> execConf = convert(execution);
        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();
        commandGeneratorModels.add(new CommandGeneratorModel(true, "spark-k8s-submit.yaml.ftl", "spark3-k8s-submit.yaml", execConf));
        return commandGeneratorModels;
    }

    private ExecFtlModel<Spark3K8sDataModel> convert(EngineExecutionModel execution) {
        String executionConf = execution.getConfiguration();
        ExecFtlModel<Spark3K8sDataModel> execModel = new ExecFtlModel<>();
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
        Spark3K8sDataModel jobConf = JsonUtils.parseObject(executionConf, Spark3K8sDataModel.class, null);
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
