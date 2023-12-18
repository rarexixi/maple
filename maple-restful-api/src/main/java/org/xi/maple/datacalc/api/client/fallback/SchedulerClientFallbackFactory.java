package org.xi.maple.datacalc.api.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.MapleExceptionUtils;
import org.xi.maple.datacalc.api.client.SchedulerClient;

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
        };
    }
}
