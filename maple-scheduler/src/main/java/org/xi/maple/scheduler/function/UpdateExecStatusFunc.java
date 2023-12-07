package org.xi.maple.scheduler.function;

import org.springframework.stereotype.Component;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.scheduler.client.PersistenceClient;

import java.util.function.BiFunction;

@Component
public class UpdateExecStatusFunc implements BiFunction<Integer, String, Integer> {

    private final PersistenceClient persistenceClient;

    public UpdateExecStatusFunc(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @Override
    public Integer apply(Integer id, String status) {
        EngineExecutionUpdateStatusRequest request = new EngineExecutionUpdateStatusRequest(id, status);
        return persistenceClient.updateExecutionStatusById(request);
    }
}
