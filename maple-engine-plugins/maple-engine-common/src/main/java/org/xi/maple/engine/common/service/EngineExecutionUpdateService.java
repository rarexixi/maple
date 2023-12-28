package org.xi.maple.engine.common.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.engine.common.utils.ParamsUtils;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;

import java.io.IOException;

/**
 * @author xishihao
 */
public class EngineExecutionUpdateService {

    public static void updateStatus(EngineExecutionStatus status) {
        Integer execId = ParamsUtils.EXEC_ID;
        String updateExecStatusUrl = ParamsUtils.UPDATE_EXEC_STATUS_URL;
        EngineExecutionUpdateStatusRequest request = new EngineExecutionUpdateStatusRequest(status.toString());
        post(execId, updateExecStatusUrl, request);
    }

    public static void updateInfo(String info) {
        Integer execId = ParamsUtils.EXEC_ID;
        String updateExecInfoUrl = ParamsUtils.UPDATE_EXEC_INFO_URL;
        EngineExecutionUpdateRequest request = new EngineExecutionUpdateRequest();
        request.setExtInfo(info);
        post(execId, updateExecInfoUrl, request);
    }

    private static void post(int execId, String url, Object data) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url + "/" + execId); // todo
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setEntity(new StringEntity(JsonUtils.toJsonString(data)));

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {

            } else {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
