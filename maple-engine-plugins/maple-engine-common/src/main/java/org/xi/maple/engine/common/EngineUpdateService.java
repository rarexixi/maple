package org.xi.maple.engine.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author xishihao
 */
public class EngineUpdateService {

    private volatile static EngineUpdateService service;

    private String updateUrl;

    private EngineUpdateService(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public static EngineUpdateService getInstance(String updateUrl) {
        if (service == null) {
            synchronized (EngineUpdateService.class) {
                if (service == null) {
                    service = new EngineUpdateService(updateUrl);
                }
            }
        }
        return service;
    }

    public void updateAddress(int engineId, String address) {
        EngineUpdateModel model =
                new EngineUpdateModel()
                        .setId(engineId)
                        .setAddress(address);
        update(model);
    }

    public void update(EngineUpdateModel model) {
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
