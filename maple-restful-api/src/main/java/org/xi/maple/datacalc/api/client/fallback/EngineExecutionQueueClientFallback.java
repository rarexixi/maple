package org.xi.maple.datacalc.api.client.fallback;

import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.client.EngineExecutionQueueClient;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;

import java.util.List;

@Service
public class EngineExecutionQueueClientFallback implements EngineExecutionQueueClient {
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
}
