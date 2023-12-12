package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.Spark3EngineExecution;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ClusterCategory("k8s")
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3K8sConvertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getCommandGenerator(EngineExecutionModel execution) {
        Spark3EngineExecution execConf;
        try {
            execConf = convert(execution);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();

        commandGeneratorModels.add(new CommandGeneratorModel(true, "spark-to-k8s.yaml.ftl", "spark-to-k8s.yaml", execConf));
        return commandGeneratorModels;
    }

    private Spark3EngineExecution convert(EngineExecutionModel execution) throws IOException {
        String executionConf = execution.getConfiguration();
        Spark3EngineExecution spark3EngineExecution = JsonUtils.parseObject(executionConf, Spark3EngineExecution.class);
        assert spark3EngineExecution != null;
        spark3EngineExecution.setName(execution.getExecName());
        spark3EngineExecution.setQueue(execution.getClusterQueue());
        spark3EngineExecution.setProxyUser(execution.getUser());
        return spark3EngineExecution;
    }
}
