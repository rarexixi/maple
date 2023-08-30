package org.xi.maple.execution.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;

import java.util.List;

@Component
public class PersistenceClientFallbackFactory implements FallbackFactory<PersistenceClient> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceClientFallbackFactory.class);

    @Override
    public PersistenceClient create(Throwable cause) {
        return new PersistenceClient() {

            @Override
            public Integer addExecution(EngineExecutionAddRequest engineExecution) {
                return null;
            }

            @Override
            public EngineExecutionDetailResponse getExecutionById(Integer id) {
                return null;
            }

            @Override
            public Integer updateExecutionStatusById(EngineExecutionUpdateStatusRequest updateStatusRequest) {
                return null;
            }

            @Override
            public Integer updateExecutionExtInfoById(EngineExecutionUpdateRequest updateRequest) {
                return null;
            }

            @Override
            public Integer addOrUpdateExecQueue(EngineExecutionQueueSaveRequest saveRequest) {
                return null;
            }

            @Override
            public Integer deleteExecQueue(String queueName) {
                return null;
            }

            @Override
            public EngineExecutionQueue getExecQueueByName(String queueName) {
                return null;
            }

            @Override
            public List<EngineExecutionQueue> getExecQueueList(EngineExecutionQueueQueryRequest queryRequest) {
                return null;
            }

            @Override
            public List<ClusterListItemResponse> getClusterList(ClusterQueryRequest queryRequest) {
                return null;
            }
        };
    }
}
