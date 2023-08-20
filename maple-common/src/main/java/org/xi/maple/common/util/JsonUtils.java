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

/**
 * @author xishihao
 */
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

    public static ObjectMapper getDateTimeObjectMapper(MapleJsonFormatProperties jsonFormatProperties) {

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(jsonFormatProperties.getDateTimeFormat());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(jsonFormatProperties.getDateFormat());
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(jsonFormatProperties.getTimeFormat());

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormat));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormat));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormat));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormat));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormat));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static ObjectMapper getDateTimeObjectMapper() {

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(MapleJsonFormatProperties.DATETIME_FORMAT);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(MapleJsonFormatProperties.DATE_FORMAT);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(MapleJsonFormatProperties.TIME_FORMAT);

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormat));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormat));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormat));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormat));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormat));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
