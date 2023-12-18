package org.xi.maple.common.util;

import feign.FeignException;
import feign.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.model.FeignResponseError;
import org.xi.maple.common.model.ResponseError;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class MapleExceptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(MapleExceptionUtils.class);

    public static Optional<FeignResponseError> getFeignResponseError(Throwable cause) {
        if (cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;

            if (feignException.hasRequest()) {
                String responseBody = feignException.contentUTF8();
                Request request = feignException.request();
                String requestBody = request.body() == null ? "" : new String(request.body(), StandardCharsets.UTF_8);
                logger.error("调用 {} 失败, 请求方法: {}, 请求体: {}, 响应数据: {}",
                        request.url(), request.httpMethod(), requestBody, responseBody);

                ResponseError responseError = JsonUtils.parseObject(responseBody, ResponseError.class, null);
                return Optional.of(new FeignResponseError(responseError, feignException));
            } else {
                logger.error("feign 调用失败, 没有 request", cause);
            }
        } else {
            logger.error("feign 调用失败", cause);
        }
        return Optional.empty();
    }
}
