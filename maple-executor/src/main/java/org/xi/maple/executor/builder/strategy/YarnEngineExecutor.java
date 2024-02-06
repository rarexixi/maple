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
import org.xi.maple.executor.configuration.ExecutionProperties;
import org.xi.maple.executor.configuration.PluginProperties;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.util.List;

@Component
public class YarnEngineExecutor extends EngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(YarnEngineExecutor.class);

    public YarnEngineExecutor(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        super(logger, enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
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
        String startFile = null;
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getId()));
        try {
            for (CommandGeneratorModel generatorModel : commandGenerators) {
                String ftlPath = generatorModel.getFtlPath();
                String fileName = generatorModel.getFilePath();
                generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel());
                if (generatorModel.isStartCommand()) {
                    startFile = fileName;
                }
            }
        } catch (Throwable t) {
            logger.error("Generate file failed!", t);
            updateExecutionStatus(execution.getId(), EngineExecutionStatus.FAILED);
        }

        ProcessBuilder processBuilder = new ProcessBuilder("sh", getPath(execHome, startFile));

        threadPoolTaskExecutor.submit(() -> {
            Process process = null;
            try {
                process = processBuilder.start();
                int exitcode = process.waitFor();
                if (exitcode != 0) {
                    updateExecutionStatus(execution.getId(), EngineExecutionStatus.FAILED);
                }
            } catch (Throwable t) {
                logger.error("Execution[" + execution.getId() + "] starts failed!", t);
                updateExecutionStatus(execution.getId(), EngineExecutionStatus.FAILED);
            } finally {
                if (process != null && process.isAlive()) {
                    ActionUtils.executeQuietly(process::destroyForcibly);
                }
            }
        });
    }
}
