package org.xi.maple.datacalc.api.service.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.service.ClusterService;

import java.io.IOException;
import java.util.List;

// todo: implement this
@Service
public class ClusterServiceImpl implements ClusterService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterServiceImpl.class);

    final EurekaClient eurekaClient;

    public ClusterServiceImpl(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public int refresh() {
        List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("maple-scheduler", false);
        for (InstanceInfo instance : instances) {
            String url = instance.getHomePageUrl() + "/cluster/refresh";
        }
        return 0;
    }

    private int refreshCluster(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return 1;
            } else {
                logger.error("刷新集群失败" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("获取 Yarn 队列信息失败", e);
        }
        return 0;
    }

    @Override
    public int refresh(String clusterName) {
        return 0;
    }
}
