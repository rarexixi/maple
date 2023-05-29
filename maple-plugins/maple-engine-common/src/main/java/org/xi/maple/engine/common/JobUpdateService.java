package org.xi.maple.engine.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author xishihao
 */
public class JobUpdateService {

    private volatile static JobUpdateService service;

    private String updateUrl;

    private JobUpdateService(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public static JobUpdateService getInstance(String updateUrl) {
        if (service == null) {
            synchronized (JobUpdateService.class) {
                if (service == null) {
                    service = new JobUpdateService(updateUrl);
                }
            }
        }
        return service;
    }

    public void update(JobUpdateModel model) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(updateUrl);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
//            httpPost.setEntity(new StringEntity(JsonUtils.toJson(model)));

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
            } else {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
