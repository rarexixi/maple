package org.xi.maple.builder.convertor;

import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.Spark3EngineExecution;
import org.xi.maple.common.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spark3Convertor implements MapleConvertor {

    @Override
    public List<CommandGeneratorModel> getCommandGenerator(EngineExecutionModel execution) {
        Spark3EngineExecution execConf = null;
        try {
            execConf = convert(execution);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CommandGeneratorModel> commandGeneratorModels = new ArrayList<>();

        commandGeneratorModels.add(new CommandGeneratorModel(true, "spark-sbumit-to-yarn.sh.ftl", "spark3-sbumit-to-yarn.sh", execConf));
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
