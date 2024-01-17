package org.xi.maple.rest.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.service.util.MapleExceptionUtils;
import org.xi.maple.rest.client.SchedulerClient;

import java.util.Map;

@Component
public class SchedulerClientFallbackFactory implements FallbackFactory<SchedulerClient> {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerClientFallbackFactory.class);

    @Override
    public SchedulerClient create(Throwable cause) {
        return new SchedulerClient() {
            @Override
            public void submitExecution(int execId) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
            }

            @Override
            public Object killExecution(Integer id) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
                return null;
            }

            @Override
            public Object stopExecution(Integer id, Map<String, ?> cancelParams) {
                MapleExceptionUtils.getFeignResponseError(cause).ifPresent(feignResponseError -> {
                    throw new MapleException(feignResponseError.getError().getMsg());
                });
                return null;
            }
        };
    }
}
