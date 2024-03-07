package org.xi.maple.datacalc.flink.util;

import org.apache.flink.table.api.TableEnvironment;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.api.MaplePlugin;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.sink.*;
import org.xi.maple.datacalc.flink.source.*;
import org.xi.maple.datacalc.flink.transform.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class PluginUtil {

    private final static Map<String, Class<?>> SOURCE_PLUGINS = getSourcePlugins();
    private final static Map<String, Class<?>> TRANSFORM_PLUGINS = getTransformPlugins();
    private final static Map<String, Class<?>> SINK_PLUGINS = getSinkPlugins();

    private static Map<String, Class<?>> getSourcePlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("custom", CustomSource.class);
        classMap.put("kafka", KafkaSource.class);
        classMap.put("jdbc", JdbcSource.class);
        return classMap;
    }

    private static Map<String, Class<?>> getTransformPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("sql", CreateViewTransform.class);
        return classMap;
    }

    private static Map<String, Class<?>> getSinkPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("custom", CustomSink.class);
        classMap.put("kafka", KafkaSink.class);
        classMap.put("jdbc", JdbcSink.class);
        return classMap;
    }

    public static <T extends MaplePluginConfig> MaplePlugin<T> createExecution(String executionType, String name, Map<String, Object> config, TableEnvironment tableEnv, Map<String, String> variables) {
        return createExecution(ExecutionType.valueOf(executionType.toUpperCase()), name, config, tableEnv, variables);
    }

    public static <T extends MaplePluginConfig> MaplePlugin<T> createExecution(ExecutionType executionType, String name, Map<String, Object> config, TableEnvironment tableEnv, Map<String, String> variables) {
        switch (executionType) {
            case SOURCE:
                return createPlugin(SOURCE_PLUGINS, name, config, tableEnv, variables);
            case TRANSFORM:
                return createPlugin(TRANSFORM_PLUGINS, name, config, tableEnv, variables);
            case SINK:
                return createPlugin(SINK_PLUGINS, name, config, tableEnv, variables);
            default:
                throw new ConfigRuntimeException("[" + executionType + "] is not a valid type");
        }
    }

    static <C extends MaplePluginConfig, T extends MaplePlugin<C>> T createPlugin(Map<String, Class<?>> pluginMap, String name, Map<String, Object> config, TableEnvironment tableEnv, Map<String, String> variables) {
        try {
            Class<?> pluginClass = pluginMap.get(name);
            if (pluginClass == null) {
                throw new IllegalArgumentException("Plugin name not found in the map: " + name);
            }
            ParameterizedType genericSuperclass = (ParameterizedType) pluginClass.getAnnotatedSuperclass().getType();
            Class<?> configType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
            T plugin = (T) pluginClass.getDeclaredConstructor(TableEnvironment.class, Map.class).newInstance(tableEnv, variables);
            plugin.setConfig((C) JsonUtils.convertValue(config, configType));
            return plugin;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new ConfigRuntimeException("Failed to create plugin: " + name, e);
        }
    }

    public static void main(String[] args) {
        Class<?> pluginClass = JdbcSink.class;
        ParameterizedType genericSuperclass = (ParameterizedType) pluginClass.getAnnotatedSuperclass().getType();
        Class<?> configType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        System.out.println(configType);
    }

    public enum ExecutionType {
        SOURCE, TRANSFORM, SINK;
    }
}
