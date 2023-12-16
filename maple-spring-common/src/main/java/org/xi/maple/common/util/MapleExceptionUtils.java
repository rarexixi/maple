package org.xi.maple.common.util;

import feign.FeignException;
import org.xi.maple.common.model.FeignResponseError;
import org.xi.maple.common.model.ResponseError;

import java.util.Optional;

public class MapleExceptionUtils {
    public static Optional<FeignResponseError> getFeignResponseError(Throwable cause) {
        if (cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;
            String responseBody = feignException.contentUTF8();
            ResponseError responseError = JsonUtils.parseObject(responseBody, ResponseError.class, null);
            return Optional.of(new FeignResponseError(responseError, feignException));
        }
        return Optional.empty();
    }
}
