package org.xi.maple.datacalc.service.strategy;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DataSourceStrategy {

    public abstract String getJdbcUrl(
            String address, Map<String, String> paramsJson, String paramsStr);

    public abstract String defaultDriver();

    protected String getConnectParams(String paramsStr) {
        if (StringUtils.isBlank(paramsStr)) return "";

        Map<String, String> paramsMap = JsonUtils.parseObject(paramsStr, Map.class, new HashMap<String, String>());
        if (paramsMap.isEmpty()) return "";

        String paramsSplitCharacter = getParamsSplitCharacter();
        String params =
                paramsMap.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(paramsSplitCharacter));
        return getParamsStartCharacter() + params;
    }

    protected String getParamsStartCharacter() {
        return "?";
    }

    protected String getParamsSplitCharacter() {
        return "&";
    }
}
