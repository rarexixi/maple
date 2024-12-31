package org.xi.maple.rest.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xi.maple.rest.client.PersistenceClient;
import org.xi.maple.rest.service.MapleAppService;
import org.xi.maple.persistence.model.response.ApplicationDetailResp;

@Service
public class MapleAppServiceImpl implements MapleAppService {

    final PersistenceClient persistenceClient;

    public MapleAppServiceImpl(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @Cacheable(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    public String getAppKey(String appName) {
        ApplicationDetailResp app = persistenceClient.getApplicationByAppName(appName);
        return app == null ? "" : app.getAccessKey();
    }
}
