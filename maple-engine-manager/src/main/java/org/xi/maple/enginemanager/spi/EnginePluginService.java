package org.xi.maple.enginemanager.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.builder.convertor.MapleConvertor;

import java.io.File;
import java.lang.annotation.Annotation;
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

    Map<String, MapleConvertor> convertorMap;

    public MapleConvertor getConvertor(String engineCategory, String engineVersion) {
        return convertorMap.get(engineCategory + "::" + engineVersion);
    }

    public void refreshPluginConvertors() {
        Map<String, MapleConvertor> convertors = new HashMap<>();
        File dir = new File("");
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("");
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

        URLClassLoader classLoader = null;
        try {
            classLoader = new URLClassLoader(pluginJars);
            ServiceLoader<MapleConvertor> loader = ServiceLoader.load(MapleConvertor.class, classLoader);
            for (MapleConvertor convertor : loader) {
                Annotation[] annotations = convertor.getClass().getAnnotations(); // 获取类级别的注解
                convertors.put("", convertor);
            }
        } catch (Exception e) {
            logger.error("Load plugin failed", e);
        } finally {
            if (classLoader != null) {
                try {
                    classLoader.close();
                } catch (Exception e) {
                    logger.error("Close class loader failed", e);
                }
            }
        }
        this.convertorMap = convertors;
    }
}
