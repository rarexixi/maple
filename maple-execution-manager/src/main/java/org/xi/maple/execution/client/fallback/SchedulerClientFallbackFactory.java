package org.xi.maple.execution.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.execution.client.SchedulerClient;

import java.util.List;
import java.util.Map;

@Component
public class SchedulerClientFallbackFactory implements FallbackFactory<SchedulerClient> {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerClientFallbackFactory.class);

    @Override
    public SchedulerClient create(Throwable cause) {
        return new SchedulerClient() {
            @Override
            public List<Map<String, ?>> deploy(String clusterName, String yaml) {
                return null;
            }
        };
    }
}
