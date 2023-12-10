package org.xi.maple.execution.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.execution.builder.spi.EnginePluginService;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.configuration.ExecutionProperties;
import org.xi.maple.execution.configuration.PluginProperties;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;


@Component
public class K8sEngineBuilder extends DefaultEngineBuilder<Object> {

    private static final Logger logger = LoggerFactory.getLogger(K8sEngineBuilder.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public K8sEngineBuilder(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        super(enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
    }

    public String execute(EngineExecutionDetailResponse execution) {
        String url = ""; // todo

        updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTING);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCode() != HttpStatus.OK && forEntity.getStatusCode() != HttpStatus.CREATED && forEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            logger.error("Execution[{}] starts failed, message: {}!", execution.getId(), forEntity.getBody());
            updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTED_FAILED);
        }
        return forEntity.getBody();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
