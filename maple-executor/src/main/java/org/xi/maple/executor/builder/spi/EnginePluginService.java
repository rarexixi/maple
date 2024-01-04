package org.xi.maple.executor.builder.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.xi.maple.builder.annotation.*;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.executor.configuration.PluginProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.function.Function;

@Service
public class EnginePluginService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EnginePluginService.class);

    private final PluginProperties pluginProperties;

    URLClassLoader classLoader = null;
    ServiceLoader<MapleConvertor> serviceLoader = null;

    public EnginePluginService(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
        refreshPluginURLs();
    }

    public MapleConvertor getConvertor(String clusterCategory, String engineCategory, String engineVersion) {
        if (serviceLoader == null) {
            return null;
        }
        for (MapleConvertor convertor : serviceLoader) {
            Class<? extends MapleConvertor> convertorClass = convertor.getClass();
            ClusterCategory clusterCategoryAnnotation = convertorClass.getAnnotation(ClusterCategory.class);
            EngineCategory engineCategoryAnnotation = convertorClass.getAnnotation(EngineCategory.class);
            EngineVersion engineVersionAnnotation = convertorClass.getAnnotation(EngineVersion.class);
            if (clusterCategoryAnnotation == null || engineCategoryAnnotation == null || engineVersionAnnotation == null) {
                continue;
            }
            if (clusterCategory.equals(clusterCategoryAnnotation.value())
                    && engineCategory.equals(engineCategoryAnnotation.value())
                    && Arrays.asList(engineVersionAnnotation.value()).contains(engineVersion)) {
                return convertor;
            }
        }
        return null;
    }

    public MapleConvertor getConvertor(String clusterCategory, String engineCategory, String engineVersion, Runnable notExistCallback) {
        MapleConvertor convertor = getConvertor(clusterCategory, engineCategory, engineVersion);
        if (convertor == null) {
            notExistCallback.run();
            throw new MapleException(String.format("不支持的引擎类型, clusterCategory: %s, engineCategory: %s, engineVersion: %s", clusterCategory, engineCategory, engineVersion));
        }
        return convertor;
    }

    public void refreshPluginURLs() {

        File dir = new File(pluginProperties.getHome());
        if (!dir.exists() || !dir.isDirectory()) {
            throw new MapleException(String.format("[%s] 不存在或者不是一个文件夹", dir.getAbsolutePath())); // todo 修改文文件异常
        }

        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            throw new MapleException("没有可用插件"); // todo 修改文件夹为空异常
        }

        Function<File, URL> fileToURL = file -> {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        };
        URL[] pluginJars = Arrays.stream(files)
                .filter(file -> file.isFile() && file.getPath().endsWith(".jar"))
                .map(fileToURL)
                .toArray(URL[]::new);

        URLClassLoader classLoader = this.classLoader;
        try {
            this.classLoader = new URLClassLoader(pluginJars);
            this.serviceLoader = ServiceLoader.load(MapleConvertor.class, this.classLoader);
        } catch (Exception e) {
            logger.error("加载插件失败", e);
        }
        closeClassLoader(classLoader);
    }

    public void closeClassLoader(URLClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        try {
            classLoader.close();
        } catch (IOException e) {
            logger.error("关闭插件类加载器失败", e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.closeClassLoader(this.classLoader)));
    }
}
