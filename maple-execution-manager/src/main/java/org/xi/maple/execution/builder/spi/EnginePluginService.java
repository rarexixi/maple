package org.xi.maple.execution.builder.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.builder.annotation.*;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.execution.configuration.PluginProperties;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@Service
public class EnginePluginService {

    private static final Logger logger = LoggerFactory.getLogger(EnginePluginService.class);

    private final PluginProperties pluginProperties;

    public EnginePluginService(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
        refreshPluginConvertors();
    }

    Map<String, MapleConvertor> convertorMap;

    public MapleConvertor getConvertor(String clusterCategory, String engineCategory, String engineVersion) {
        return convertorMap.get(clusterCategory + "::" + engineCategory + "::" + engineVersion);
    }

    public void refreshPluginConvertors() {
        Map<String, MapleConvertor> convertors = new HashMap<>();
        File dir = new File(pluginProperties.getHome());
        if (!dir.exists() || !dir.isDirectory()) {
            throw new MapleException(String.format("[%s] 不存在或者不是一个文件夹", dir.getAbsolutePath())); // todo 修改文文件异常
        }

        File[] files = dir.listFiles();

        if (files == null) {
            logger.warn("There is no plugins.");
            return;
        }
        URL[] pluginJars = Arrays.stream(files)
                .filter(file -> file.isFile() && file.getPath().endsWith(".jar"))
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);

        try (URLClassLoader classLoader = new URLClassLoader(pluginJars)) {
            ServiceLoader<MapleConvertor> loader = ServiceLoader.load(MapleConvertor.class, classLoader);
            for (MapleConvertor convertor : loader) {
                Class<? extends MapleConvertor> convertorClass = convertor.getClass();
                ClusterCategory clusterCategory = convertorClass.getAnnotation(ClusterCategory.class);
                EngineCategory engineCategory = convertorClass.getAnnotation(EngineCategory.class);
                EngineVersion engineVersion = convertorClass.getAnnotation(EngineVersion.class);
                for (String version : engineVersion.value()) {
                    convertors.put(clusterCategory.value() + "::" + engineCategory.value() + "::" + version, convertor);
                }
            }
        } catch (Exception e) {
            logger.error("Load plugin failed", e);
        }
        this.convertorMap = convertors;
    }
}
