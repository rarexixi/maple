package org.xi.maple.executor.service;

import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;

public interface EngineExecutionService {
    void execute(EngineExecutionDetailResp execution);
}
