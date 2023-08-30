package org.xi.maple.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.model.MapleJsonFormatProperties;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author xishihao
 */
public class JsonUtils {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = getDateTimeObjectMapper();

    public static <T> String toJsonString(T t) throws JsonProcessingException {
        return customToJsonString(t, DEFAULT_OBJECT_MAPPER);
    }

    public static <T> String toJsonString(T t, String defaultValue) {
        try {
            return customToJsonString(t, DEFAULT_OBJECT_MAPPER);
        } catch (JsonProcessingException e) {
            return defaultValue;
        }
    }

    public static <T> String customToJsonString(T t, ObjectMapper mapper) throws JsonProcessingException {
        if (t == null || mapper == null) {
            return null;
        }
        return mapper.writeValueAsString(t);
    }

    public static <T> T parseObject(String json, Class<T> clazz) throws IOException {
        return customParseObject(json, clazz, DEFAULT_OBJECT_MAPPER);
    }

    public static <T> T parseObject(String json, Class<T> clazz, T defaultValue) {
        try {
            return customParseObject(json, clazz, DEFAULT_OBJECT_MAPPER);
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public static <T> T customParseObject(String json, Class<T> clazz, ObjectMapper mapper) throws IOException {
        if (StringUtils.isBlank(json) || mapper == null) {
            return null;
        }
        return mapper.readValue(json, clazz);
    }

    public static <T> T convertValue(Object fromValue, Class<T> clazz) {
        return customConvertValue(fromValue, clazz, DEFAULT_OBJECT_MAPPER);
    }

    public static <T> T convertValue(Object fromValue, Class<T> clazz, T defaultValue) {
        try {
            return customConvertValue(fromValue, clazz, DEFAULT_OBJECT_MAPPER);
        } catch (Throwable ignored) {
            return defaultValue;
        }
    }

    public static <T> T customConvertValue(Object fromValue, Class<T> clazz, ObjectMapper mapper) {
        if (fromValue == null || mapper == null) {
            return null;
        }
        return mapper.convertValue(fromValue, clazz);
    }

    public static ObjectMapper getDateTimeObjectMapper(MapleJsonFormatProperties jsonFormatProperties) {
        return getDateTimeObjectMapper(jsonFormatProperties.getDateTimeFormat(),
                jsonFormatProperties.getDateFormat(),
                jsonFormatProperties.getTimeFormat());
    }

    public static ObjectMapper getDateTimeObjectMapper() {
        return getDateTimeObjectMapper(MapleJsonFormatProperties.DATETIME_FORMAT,
                MapleJsonFormatProperties.DATE_FORMAT,
                MapleJsonFormatProperties.TIME_FORMAT);
    }

    public static ObjectMapper getDateTimeObjectMapper(String datetimeFormatStr, String dateFormatStr, String timeFormatStr) {

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(datetimeFormatStr);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(dateFormatStr);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(timeFormatStr);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormat));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormat));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormat));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormat));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormat));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 匹配不上的字段忽略
        return objectMapper;
    }
}
