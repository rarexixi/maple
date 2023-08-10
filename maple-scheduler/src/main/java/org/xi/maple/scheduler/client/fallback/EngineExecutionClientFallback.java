package org.xi.maple.scheduler.client.fallback;

import org.springframework.stereotype.Service;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.scheduler.client.EngineExecutionClient;

@Service
public class EngineExecutionClientFallback implements EngineExecutionClient {
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
}
