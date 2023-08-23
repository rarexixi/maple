package org.xi.maple.persistence.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.xi.maple.common.model.MapleJsonFormatProperties;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.configuration.properties.MapleProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class MapleJacksonConfiguration {

    final MapleJsonFormatProperties jsonFormatProperties;

    public MapleJacksonConfiguration(MapleProperties mapleProperties) {
        this.jsonFormatProperties = mapleProperties.getJsonFormat();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonUtils.getDateTimeObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /*@Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getDateTimeFormat())))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getDateFormat())))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getTimeFormat())))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getDateTimeFormat())))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getDateFormat())))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(jsonFormatProperties.getTimeFormat())));
    }*/

    // region 用于转换RequestParam和PathVariable参数

    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(jsonFormatProperties.getDateFormat()));
            }
        };
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(jsonFormatProperties.getDateTimeFormat()));
            }
        };
    }

    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(jsonFormatProperties.getTimeFormat()));
            }
        };
    }

    // endregion
}
