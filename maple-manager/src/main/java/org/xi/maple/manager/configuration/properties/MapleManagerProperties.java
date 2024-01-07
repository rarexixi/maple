package org.xi.maple.manager.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "maple.manager")
@Data
public class MapleManagerProperties {
    private boolean refreshYarn = false;
    private long yarnRefreshPeriod = 10 * 60 * 1000;

    private boolean refreshK8s = false;
    private long k8sRefreshPeriod = 10 * 60 * 1000;

    private boolean consumeJob = false;
    private long consumeJobPeriod = 10 * 60 * 1000;
}
