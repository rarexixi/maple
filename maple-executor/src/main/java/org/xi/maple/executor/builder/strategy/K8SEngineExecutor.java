package org.xi.maple.executor.builder.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.executor.builder.EngineExecutor;
import org.xi.maple.executor.builder.spi.EnginePluginService;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.executor.client.SchedulerClient;
import org.xi.maple.executor.configuration.ExecutionProperties;
import org.xi.maple.executor.configuration.PluginProperties;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class K8SEngineExecutor extends EngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(K8SEngineExecutor.class);

    private final SchedulerClient schedulerClient;

    public K8SEngineExecutor(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, SchedulerClient schedulerClient) {
        super(logger, enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
        this.schedulerClient = schedulerClient;
    }

    public void execute(EngineExecutionDetailResponse execution) {

        updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTING);
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion(), () -> {
            logger.error("Execution[" + execution.getId() + "] starts failed!");
            updateExecutionStatus(execution.getId(), EngineExecutionStatus.START_FAILED);
        });

        List<CommandGeneratorModel> commandGenerators = convertor.getSubmitCommandGenerator(convert(execution));
        if (commandGenerators == null || commandGenerators.isEmpty()) {
            throw new MapleException(""); // todo
        }
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getId()));
        List<String> yamlFiles = new ArrayList<>(commandGenerators.size());
        for (CommandGeneratorModel generatorModel : commandGenerators) {
            String ftlPath = generatorModel.getFtlPath();
            String fileName = generatorModel.getFilePath();
            ActionUtils.executeQuietly(() -> generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel()));
            yamlFiles.add(fileName);
        }

        for (String yamlPath : yamlFiles) {
            ActionUtils.executeQuietly(() -> deploy(execution.getCluster(), yamlPath));
        }
    }

    private List<Map<String, ?>> deploy(String cluster, String yamlPath) throws IOException {
        // todo 拦截错误信息，修改状态
        return schedulerClient.deploy(cluster, Files.readString(Paths.get(yamlPath)));
    }
}
