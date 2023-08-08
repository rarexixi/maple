package org.xi.maple.datacalc.util;

import org.xi.maple.datacalc.api.MaplePlugin;
import org.xi.maple.datacalc.api.MapleSink;
import org.xi.maple.datacalc.api.MapleSource;
import org.xi.maple.datacalc.api.MapleTransform;
import org.xi.maple.datacalc.model.SinkConfig;
import org.xi.maple.datacalc.model.SourceConfig;
import org.xi.maple.datacalc.model.TransformConfig;
import org.xi.maple.datacalc.sink.*;
import org.xi.maple.datacalc.source.*;
import org.xi.maple.datacalc.transform.*;

import java.io.IOException;
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
        classMap.put("managed_jdbc", org.xi.maple.datacalc.source.ManagedJdbcSource.class);
        classMap.put("jdbc", JdbcSource.class);
        classMap.put("file", FileSource.class);
        return classMap;
    }

    private static Map<String, Class<?>> getTransformPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("sql", SqlTransform.class);
        return classMap;
    }

    private static Map<String, Class<?>> getSinkPlugins() {
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("managed_jdbc", org.xi.maple.datacalc.sink.ManagedJdbcSink.class);
        classMap.put("jdbc", JdbcSink.class);
        classMap.put("hive", HiveSink.class);
        classMap.put("file", FileSink.class);
        return classMap;
    }
    
    public static <T extends SourceConfig> MapleSource<T> createSource(String name, Map<String, Object> config) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(SOURCE_PLUGINS, name, config);
    }

    public static <T extends TransformConfig> MapleTransform<T> createTransform(String name, Map<String, Object> config) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(TRANSFORM_PLUGINS, name, config);
    }

    public static <T extends SinkConfig> MapleSink<T> createSink(String name, Map<String, Object> config) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(SINK_PLUGINS, name, config);
    }

    static <T extends MaplePlugin> T createPlugin(Map<String, Class<?>> pluginMap, String name, Map<String, Object> config) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> type = pluginMap.get(name);
        ParameterizedType genericSuperclass = (ParameterizedType) type.getGenericInterfaces()[0];
        Class<?> configType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        T plugin = (T) type.getDeclaredConstructor().newInstance();
        try {
            plugin.setConfig(JsonUtils.convertValue(config, configType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return plugin;
    }
}
