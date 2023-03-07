package org.xi.maple.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class JsonUtils {

    public static <T> String toJsonString(T t) throws JsonProcessingException {
        if (t == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(t);
    }

    public static <T> String toJsonString(T t, String defaultValue) {
        try {
            return toJsonString(t);
        } catch (JsonProcessingException e) {
            return defaultValue;
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 匹配不上的字段忽略
        return mapper.readValue(json, clazz);
    }

    public static <T> T parseObject(String json, Class<T> clazz, T defaultValue) {
        try {
            return parseObject(json, clazz);
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public static <T> String toJsonString(T t, ObjectMapper mapper) throws JsonProcessingException {
        if (t == null) {
            return null;
        }
        return mapper.writeValueAsString(t);
    }

    public static <T> T parseObject(String json, Class<T> clazz, ObjectMapper mapper) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return mapper.readValue(json, clazz);
    }
}
