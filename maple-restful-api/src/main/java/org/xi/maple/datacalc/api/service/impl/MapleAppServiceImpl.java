package org.xi.maple.datacalc.api.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.client.PersistenceClient;
import org.xi.maple.datacalc.api.service.MapleAppService;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;

@Service
public class MapleAppServiceImpl implements MapleAppService {

    final PersistenceClient persistenceClient;

    public MapleAppServiceImpl(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @Cacheable(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    public String getAppKey(String appName) {
        ApplicationDetailResponse app = persistenceClient.getByAppName(appName);
        return app == null ? "" : app.getAccessKey();
    }
}
