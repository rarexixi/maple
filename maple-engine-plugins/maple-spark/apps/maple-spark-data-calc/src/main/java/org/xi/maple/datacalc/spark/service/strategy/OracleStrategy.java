package org.xi.maple.datacalc.spark.service.strategy;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class OracleStrategy extends DataSourceStrategy {

    @Override
    public String getJdbcUrl(String address, Map<String, String> paramsJson, String paramsStr) {
        String serviceName = paramsJson.getOrDefault("serviceName", "");
        String server = paramsJson.getOrDefault("server", "");
        String instanceName = paramsJson.getOrDefault("instance", "");
        String sid = paramsJson.getOrDefault("sid", "");
        StringBuilder builder = new StringBuilder("jdbc:oracle:thin:@");
        if (StringUtils.isNotBlank(sid)) {
            builder.append(address);
            builder.append(":").append(sid);
        } else {
            builder.append("//").append(address).append("/").append(serviceName);
            if (StringUtils.isNotBlank(server)) builder.append(":").append(server);
        }
        if (StringUtils.isNotBlank(instanceName)) builder.append("/").append(instanceName);
        if (!paramsStr.isEmpty()) builder.append(getConnectParams(paramsStr));
        return builder.toString();
    }

    @Override
    public String defaultDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
