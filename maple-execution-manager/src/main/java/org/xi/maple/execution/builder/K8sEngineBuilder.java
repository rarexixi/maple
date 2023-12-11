package org.xi.maple.execution.builder;

import io.fabric8.kubernetes.api.model.HasMetadata;
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
import org.xi.maple.execution.client.SchedulerClient;
import org.xi.maple.execution.configuration.ExecutionProperties;
import org.xi.maple.execution.configuration.PluginProperties;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Component
public class K8sEngineBuilder extends EngineBuilder<Object> {

    private static final Logger logger = LoggerFactory.getLogger(K8sEngineBuilder.class);
    @Override
    public Logger getLogger() {
        return logger;
    }

    private final SchedulerClient schedulerClient;

    public K8sEngineBuilder(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, SchedulerClient schedulerClient) {
        super(enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
        this.schedulerClient = schedulerClient;
    }

    public String execute(EngineExecutionDetailResponse execution) {

        updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTING);
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getCommandGenerator(convert(execution));
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getId()));
        List<String> files = new ArrayList<>(commandGenerators.size());
        for (CommandGeneratorModel generatorModel : commandGenerators) {
            String ftlPath = generatorModel.getFtlPath();
            String fileName = generatorModel.getFilePath();
            ActionUtils.executeQuietly(() -> generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel()));
            files.add(fileName);
        }

        for (String yamlPath : files) {
            ActionUtils.executeQuietly(() -> deploy(execution.getCluster(), yamlPath));
        }
        return null;
    }

    private List<HasMetadata> deploy(String cluster, String yamlPath) throws IOException {
        return schedulerClient.deploy(cluster, Files.readString(Paths.get(yamlPath)));
    }
}
