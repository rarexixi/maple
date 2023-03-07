package org.xi.maple.service.strategy;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SqlServerStrategy extends DataSourceStrategy {

    @Override
    public String getJdbcUrl(String address, Map<String, String> paramsJson, String paramsStr) {
        String databaseName = paramsJson.getOrDefault("databaseName", "");
        String instanceName = paramsJson.getOrDefault("instance", "");
        StringBuilder builder = new StringBuilder("jdbc:sqlserver://");
        if (StringUtils.isNotBlank(address)) builder.append(address);
        if (StringUtils.isNotBlank(instanceName)) builder.append(";instanceName=").append(instanceName);
        if (StringUtils.isNotBlank(databaseName)) builder.append(";databaseName=").append(databaseName);
        if (!paramsStr.isEmpty()) builder.append(getConnectParams(paramsStr));
        return builder.toString();
    }

    @Override
    public String defaultDriver() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    protected String getParamsStartCharacter() {
        return ";";
    }

    @Override
    protected String getParamsSplitCharacter() {
        return ";";
    }
}
