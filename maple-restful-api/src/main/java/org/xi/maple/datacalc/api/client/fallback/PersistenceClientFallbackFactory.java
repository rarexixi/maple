package org.xi.maple.datacalc.api.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.datacalc.api.client.PersistenceClient;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;

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
            public Integer updateExecutionStatusById(EngineExecutionUpdateStatusRequest updateRequest) {
                return null;
            }

            @Override
            public Integer updateExecutionInfoById(EngineExecutionUpdateRequest updateRequest) {
                return null;
            }

            @Override
            public OperateResult<Integer> addOrUpdateExecQueue(MapleEngineExecutionQueue engineExecutionQueue) {
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
            public ApplicationDetailResponse getByAppName(String appName) {
                return null;
            }
        };
    }
}
