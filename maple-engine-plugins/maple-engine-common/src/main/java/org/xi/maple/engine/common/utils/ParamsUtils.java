package org.xi.maple.engine.common.utils;

import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.PropertiesUtils;

import java.io.FileNotFoundException;
import java.util.Properties;

public class ParamsUtils {
    // java -Djob-id=xxx ...
    // spark-submit --conf spark.extraListeners=org.xi.maple.spark3.listener.OnceAppListener
    //              --conf spark.driver.extraJavaOptions="-Dexec-id=xxx -Dengine-conf-path=${MAPLE_HOME}/conf/maple-plugin.properties"

    public final static Integer EXEC_ID = getExecId();
    public final static String UPDATE_EXEC_STATUS_URL;
    public final static String UPDATE_EXEC_INFO_URL;

    static {
        String engineConfPath = System.getProperty("engine-conf-path");
        try {
            Properties properties = PropertiesUtils.getProperties(engineConfPath);
            UPDATE_EXEC_STATUS_URL = properties.getProperty("update-exec-status-url");
            UPDATE_EXEC_INFO_URL = properties.getProperty("update-exec-info-url");
        } catch (FileNotFoundException e) {
            throw new MapleException("Parameter [engine-conf-path] is null or empty");
        }
    }

    public static Integer getExecId() {
        String property = System.getProperty("exec-id");
        if (property == null || "".equals(property.trim())) {
            throw new MapleException("Parameter [exec-id] is null or empty");
        }
        return Integer.valueOf(property);
    }
}
