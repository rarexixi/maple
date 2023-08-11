package org.xi.maple.scheduler.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.scheduler.client.PersistenceClient;

import java.util.List;

@Component
public class PersistenceClientFallbackFactory implements FallbackFactory<PersistenceClient> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceClientFallbackFactory.class);

    @Override
    public PersistenceClient create(Throwable cause) {
        return new PersistenceClient() {
            @Override
            public Integer add(EngineExecutionAddRequest engineExecution) {
                return null;
            }

            @Override
            public EngineExecutionDetailResponse getById(Integer id) {
                return null;
            }

            @Override
            public Integer updateStatusById(EngineExecutionUpdateStatusRequest updateRequest) {
                return null;
            }

            @Override
            public Integer addOrUpdate(EngineExecutionQueueSaveRequest engineExecutionQueue) {
                return null;
            }

            @Override
            public Integer delete(String queueName) {
                return null;
            }

            @Override
            public EngineExecutionQueue getByQueueName(String queueName) {
                return null;
            }

            @Override
            public List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest) {
                return null;
            }
        };
    }
}
