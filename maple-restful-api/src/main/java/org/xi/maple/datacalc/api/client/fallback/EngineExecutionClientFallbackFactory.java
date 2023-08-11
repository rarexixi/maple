package org.xi.maple.datacalc.api.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.datacalc.api.client.EngineExecutionClient;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

@Component
public class EngineExecutionClientFallbackFactory implements FallbackFactory<EngineExecutionClient> {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionClientFallbackFactory.class);

    @Override
    public EngineExecutionClient create(Throwable cause) {
        return new EngineExecutionClient() {
            @Override
            public Integer add(EngineExecutionAddRequest engineExecution) {
                return 0;
            }

            @Override
            public EngineExecutionDetailResponse getById(Integer id) {
                return null;
            }

            @Override
            public Integer updateStatusById(EngineExecutionUpdateStatusRequest updateRequest) {
                return null;
            }
        };
    }
}
