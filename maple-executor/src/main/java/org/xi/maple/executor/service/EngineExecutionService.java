package org.xi.maple.executor.service;

import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

public interface EngineExecutionService {
    void execute(EngineExecutionDetailResponse execution);
}
