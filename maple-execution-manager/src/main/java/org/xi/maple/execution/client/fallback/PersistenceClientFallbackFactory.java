package org.xi.maple.execution.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;

@Component
public class PersistenceClientFallbackFactory implements FallbackFactory<PersistenceClient> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceClientFallbackFactory.class);

    @Override
    public PersistenceClient create(Throwable cause) {
        return new PersistenceClient() {

            @Override
            public Integer updateExecutionStatusById(EngineExecutionUpdateStatusRequest updateStatusRequest) {
                return null;
            }

            @Override
            public Integer updateExecutionExtInfoById(EngineExecutionUpdateRequest updateRequest) {
                return null;
            }
        };
    }
}
