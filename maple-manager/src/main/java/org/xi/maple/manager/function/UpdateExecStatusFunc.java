package org.xi.maple.manager.function;

import org.springframework.stereotype.Component;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.manager.client.PersistenceClient;

import java.util.function.BiFunction;

@Component
public class UpdateExecStatusFunc implements BiFunction<Integer, EngineExecutionUpdateStatusRequest, Integer> {

    private final PersistenceClient persistenceClient;

    public UpdateExecStatusFunc(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @Override
    public Integer apply(Integer id, EngineExecutionUpdateStatusRequest request) {
        return persistenceClient.updateExecutionStatusById(id, request);
    }
}
