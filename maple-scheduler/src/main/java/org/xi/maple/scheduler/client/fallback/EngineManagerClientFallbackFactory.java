package org.xi.maple.scheduler.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.scheduler.client.EngineManagerClient;

@Component
public class EngineManagerClientFallbackFactory implements FallbackFactory<EngineManagerClient> {

    private static final Logger logger = LoggerFactory.getLogger(EngineManagerClientFallbackFactory.class);

    @Override
    public EngineManagerClient create(Throwable cause) {
        return new EngineManagerClient() {

            @Override
            public void execute(EngineExecutionDetailResponse execution) {

            }
        };
    }
}
