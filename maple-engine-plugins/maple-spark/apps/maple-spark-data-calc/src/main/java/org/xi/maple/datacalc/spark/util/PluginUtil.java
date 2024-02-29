package org.xi.maple.datacalc.spark.util;

import org.apache.spark.sql.SparkSession;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.spark.api.MaplePlugin;
import org.xi.maple.datacalc.spark.api.MapleSink;
import org.xi.maple.datacalc.spark.api.MapleSource;
import org.xi.maple.datacalc.spark.api.MapleTransform;
import org.xi.maple.datacalc.spark.model.SinkConfig;
import org.xi.maple.datacalc.spark.model.SourceConfig;
import org.xi.maple.datacalc.spark.model.TransformConfig;
import org.xi.maple.datacalc.spark.sink.*;
import org.xi.maple.datacalc.spark.source.*;
import org.xi.maple.datacalc.spark.transform.*;

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
        classMap.put("managed_jdbc", ManagedJdbcSource.class);
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
        classMap.put("managed_jdbc", ManagedJdbcSink.class);
        classMap.put("jdbc", JdbcSink.class);
        classMap.put("hive", HiveSink.class);
        classMap.put("file", FileSink.class);
        return classMap;
    }
    
    public static <T extends SourceConfig> MapleSource<T> createSource(String name, Map<String, Object> config, SparkSession spark, Map<String, String> variables) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(SOURCE_PLUGINS, name, config, spark, variables);
    }

    public static <T extends TransformConfig> MapleTransform<T> createTransform(String name, Map<String, Object> config, SparkSession spark, Map<String, String> variables) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(TRANSFORM_PLUGINS, name, config, spark, variables);
    }

    public static <T extends SinkConfig> MapleSink<T> createSink(String name, Map<String, Object> config, SparkSession spark, Map<String, String> variables) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return createPlugin(SINK_PLUGINS, name, config, spark, variables);
    }

    public static <T extends MaplePlugin> T createPlugin(Map<String, Class<?>> pluginMap, String name, Map<String, Object> config, SparkSession spark, Map<String, String> variables) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> pluginClass = pluginMap.get(name);
        if (pluginClass == null) {
            throw new IllegalArgumentException("Plugin name not found in the map: " + name);
        }
        ParameterizedType genericSuperclass = (ParameterizedType) pluginClass.getGenericInterfaces()[0];
        Class<?> configType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        T plugin = (T) pluginClass.getDeclaredConstructor().newInstance();
        plugin.setConfig(JsonUtils.convertValue(config, configType));
        plugin.setSpark(spark);
        plugin.setVariables(variables);
        return plugin;
    }
}
