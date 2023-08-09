package org.xi.maple.datacalc.api.client.fallback;

import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.client.EngineExecutionClient;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

@Service
public class EngineExecutionClientFallback implements EngineExecutionClient {
    @Override
    public Integer add(EngineExecutionAddRequest engineExecution) {
        return 0;
    }
}
