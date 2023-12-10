package org.xi.maple.execution.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.execution.builder.spi.EnginePluginService;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.configuration.ExecutionProperties;
import org.xi.maple.execution.configuration.PluginProperties;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.util.List;

@Component
public class YarnEngineBuilder extends DefaultEngineBuilder<Void> {

    private static final Logger logger = LoggerFactory.getLogger(YarnEngineBuilder.class);


    public YarnEngineBuilder(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        super(enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
    }

    public Void execute(EngineExecutionDetailResponse execution) {
        updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTING);
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getCommandGenerator(convert(execution));
        String startFile = null;
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getId()));
        for (CommandGeneratorModel generatorModel : commandGenerators) {
            String ftlPath = generatorModel.getFtlPath();
            String fileName = generatorModel.getFilePath();
            ActionUtils.executeQuietly(() -> generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel()));
            if (generatorModel.isStartCommand()) {
                startFile = fileName;
            }
        }
        ProcessBuilder processBuilder = new ProcessBuilder("sh", getPath(executionProperties.getExecHome(), startFile));

        threadPoolTaskExecutor.submit(() -> {
            Process process = null;
            try {
                process = processBuilder.start();
                int exitcode = process.waitFor();
                if (exitcode != 0) {
                    updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTED_FAILED);
                }
            } catch (Throwable t) {
                logger.error("Execution[" + execution.getId() + "] starts failed!", t);
                updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTED_FAILED);
            } finally {
                if (process != null && process.isAlive()) {
                    ActionUtils.executeQuietly(process::destroyForcibly);
                }
            }
        });
        return null;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
