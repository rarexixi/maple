package org.xi.maple.service.strategy;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public abstract class NormalStrategy extends DataSourceStrategy {

    @Override
    public String getJdbcUrl(String address, Map<String, String> paramsJson, String paramsStr) {
        String databaseName = paramsJson.getOrDefault("databaseName", "");
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:").append(this.getDatabaseType()).append("://");
        if (StringUtils.isNotBlank(address)) builder.append(address);
        if (StringUtils.isNotBlank(databaseName)) builder.append("/").append(databaseName);
        if (!paramsStr.isEmpty()) builder.append(getConnectParams(paramsStr));
        return builder.toString();
    }

    public abstract String getDatabaseType();
}
