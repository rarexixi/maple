package org.xi.maple.scheduler.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.MapleExceptionUtils;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.scheduler.client.ExecutionManagerClient;

@Component
public class ExecutionManagerClientFallbackFactory implements FallbackFactory<ExecutionManagerClient> {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionManagerClientFallbackFactory.class);

    @Override
    public ExecutionManagerClient create(Throwable cause) {
        return new ExecutionManagerClient() {

            @Override
            public void execute(EngineExecutionDetailResponse execution) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
            }
        };
    }
}
