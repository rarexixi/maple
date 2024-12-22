package org.xi.maple.datacalc.flink.util;

import org.xi.maple.datacalc.flink.api.MaplePlugin;
import org.xi.maple.datacalc.flink.model.MapleArrayData;
import org.xi.maple.datacalc.flink.model.MapleDataConfig;
import org.xi.maple.datacalc.flink.model.MaplePluginConfig;
import org.xi.maple.datacalc.flink.sink.JdbcSink;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PluginUtilTests {

    public static void main(String[] args) throws IOException {
        Class<?> pluginClass = JdbcSink.class;
        ParameterizedType genericSuperclass = (ParameterizedType) pluginClass.getAnnotatedSuperclass().getType();
        Class<?> configType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        System.out.println(configType);

        byte[] data = Files.readAllBytes(Paths.get("/home/xi/Projects/github/maple/examples/flink-data-array.json"));
        MapleArrayData mapleData = MapleArrayData.getData(new String(data));
        System.out.println(mapleData);


        List<MaplePlugin> executions = new ArrayList<>(mapleData.getPlugins().length);
        for (MapleDataConfig dc : mapleData.getPlugins()) {
            MaplePlugin<MaplePluginConfig> execution = PluginUtil.createExecution(dc.getType(), dc.getName(), dc.getConfig(), null);
            executions.add(execution);
        }
        System.out.println(executions);
    }
}
