package org.xi.maple.datacalc.api.client.fallback;

import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.client.EngineExecutionClient;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

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
