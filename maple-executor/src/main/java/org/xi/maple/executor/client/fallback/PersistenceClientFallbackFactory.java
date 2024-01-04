package org.xi.maple.executor.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.util.MapleExceptionUtils;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;

@Component
public class PersistenceClientFallbackFactory implements FallbackFactory<PersistenceClient> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceClientFallbackFactory.class);

    @Override
    public PersistenceClient create(Throwable cause) {
        return new PersistenceClient() {

            @Override
            public Integer updateExecutionStatusById(Integer id, EngineExecutionUpdateStatusRequest updateStatusRequest) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
                return null;
            }

            @Override
            public Integer updateExecutionExtInfoById(EngineExecutionUpdateRequest updateRequest) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
                return null;
            }

            @Override
            public EngineConf getEngineConf(ClusterEngineDefaultConfGetRequest request) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                });
                return null;
            }
        };
    }
}
