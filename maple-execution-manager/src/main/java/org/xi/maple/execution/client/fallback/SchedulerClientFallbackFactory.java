package org.xi.maple.execution.client.fallback;

import io.fabric8.kubernetes.api.model.HasMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xi.maple.execution.client.SchedulerClient;

import java.util.List;

@Component
public class SchedulerClientFallbackFactory implements FallbackFactory<SchedulerClient> {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerClientFallbackFactory.class);

    @Override
    public SchedulerClient create(Throwable cause) {
        return new SchedulerClient() {
            @Override
            public List<HasMetadata> deploy(String clusterName, String yaml) {
                return null;
            }
        };
    }
}
