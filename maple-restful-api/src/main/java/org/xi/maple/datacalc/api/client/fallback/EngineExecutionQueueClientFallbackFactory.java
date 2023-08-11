package org.xi.maple.datacalc.api.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.datacalc.api.client.EngineExecutionQueueClient;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;

import java.util.List;

@Component
public class EngineExecutionQueueClientFallbackFactory implements FallbackFactory<EngineExecutionQueueClient> {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionQueueClientFallbackFactory.class);

    @Override
    public EngineExecutionQueueClient create(Throwable cause) {
        return new EngineExecutionQueueClient() {
            @Override
            public Integer addOrUpdate(MapleEngineExecutionQueue engineExecutionQueue) {
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
