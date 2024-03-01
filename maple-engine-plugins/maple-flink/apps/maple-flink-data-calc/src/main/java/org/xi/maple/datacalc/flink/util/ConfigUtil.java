package org.xi.maple.datacalc.flink.util;

import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.sink.*;
import org.xi.maple.datacalc.flink.source.*;
import org.xi.maple.datacalc.flink.transform.*;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {

    private final static Map<String, Class<?>> SOURCE_PLUGINS = getSourcePlugins();
    private final static Map<String, Class<?>> TRANSFORM_PLUGINS = getTransformPlugins();
    private final static Map<String, Class<?>> SINK_PLUGINS = getSinkPlugins();

    private static Map<String, Class<?>> getSourcePlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("custom", CustomSourceConfig.class);
        classMap.put("kafka", KafkaSourceConfig.class);
        classMap.put("jdbc", JdbcSourceConfig.class);
        return classMap;
    }

    private static Map<String, Class<?>> getTransformPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("sql", SqlTransformConfig.class);
        return classMap;
    }

    private static Map<String, Class<?>> getSinkPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("custom", CustomSinkConfig.class);
        classMap.put("kafka", KafkaSinkConfig.class);
        classMap.put("jdbc", JdbcSinkConfig.class);
        return classMap;
    }

    public static <T extends MaplePluginConfig> T createConfig(String executionType, String name, Map<String, Object> config) {
        switch (ExecutionType.valueOf(executionType.toUpperCase())) {
            case SOURCE:
                return createPlugin(SOURCE_PLUGINS, name, config);
            case TRANSFORM:
                return createPlugin(TRANSFORM_PLUGINS, name, config);
            case SINK:
                return createPlugin(SINK_PLUGINS, name, config);
            default:
                throw new IllegalArgumentException("Config type not found: " + executionType);
        }
    }

    public static <T extends MaplePluginConfig> T createConfig(ExecutionType executionType, String name, Map<String, Object> config) {
        switch (executionType) {
            case SOURCE:
                return createPlugin(SOURCE_PLUGINS, name, config);
            case TRANSFORM:
                return createPlugin(TRANSFORM_PLUGINS, name, config);
            case SINK:
                return createPlugin(SINK_PLUGINS, name, config);
            default:
                throw new IllegalArgumentException("Config type not found: " + executionType);
        }
    }

    static <T extends MaplePluginConfig> T createPlugin(Map<String, Class<?>> pluginMap, String name, Map<String, Object> config) {
        Class<?> configClass = pluginMap.get(name);
        if (configClass == null) {
            throw new IllegalArgumentException("Config name not found in the map: " + name);
        }
        return (T) JsonUtils.convertValue(config, configClass);
    }


    public enum ExecutionType {
        SOURCE, TRANSFORM, SINK;
    }
}
