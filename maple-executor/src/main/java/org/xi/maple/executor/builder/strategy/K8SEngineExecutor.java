package org.xi.maple.executor.builder.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.executor.builder.BaseEngineExecutor;
import org.xi.maple.executor.builder.EngineExecutor;
import org.xi.maple.executor.builder.spi.EnginePluginService;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.executor.client.ManagerClient;
import org.xi.maple.executor.configuration.ExecutionProperties;
import org.xi.maple.executor.configuration.PluginProperties;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class K8SEngineExecutor extends BaseEngineExecutor implements EngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(K8SEngineExecutor.class);

    private final ManagerClient managerClient;

    public K8SEngineExecutor(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, ManagerClient managerClient) {
        super(enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
        this.managerClient = managerClient;
    }

    public void execute(EngineExecutionModel execution) {
        updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.STARTING);

        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion(), () -> {
            logger.error("Execution[{}] starts failed! ", execution.getExecId());
            updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.START_FAILED);
        });

        List<CommandGeneratorModel> commandGenerators = convertor.getSubmitCommandGenerator(execution);
        if (commandGenerators == null || commandGenerators.isEmpty()) {
            throw new MapleException(""); // todo
        }
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getExecId()));
        List<String> yamlFiles = new ArrayList<>(commandGenerators.size());

        try {
            for (CommandGeneratorModel generatorModel : commandGenerators) {
                String ftlPath = generatorModel.getFtlPath();
                String fileName = generatorModel.getFilePath();
                generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel());
                yamlFiles.add(fileName);
            }
            for (String yamlPath : yamlFiles) {
                String yaml = new String(Files.readAllBytes(Paths.get(getPath(execHome, yamlPath))));
                managerClient.deploy(execution.getClusterId(), yaml);
            }
        } catch (Throwable t) {
            logger.error("Generate file failed!", t);
            updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.START_FAILED);
        }
    }

    @Override
    public void operate(EngineExecutionModel execution) {
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion());

        List<CommandGeneratorModel> commandGenerators = convertor.getOperateCommandGenerator(execution);
        if (commandGenerators == null || commandGenerators.isEmpty()) {
            throw new MapleException("");
        }
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getExecId()));
        List<String> yamlFiles = new ArrayList<>(commandGenerators.size());
        // todo
    }
}
