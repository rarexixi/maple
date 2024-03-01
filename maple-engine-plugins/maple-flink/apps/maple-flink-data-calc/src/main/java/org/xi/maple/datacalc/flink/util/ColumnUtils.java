package org.xi.maple.datacalc.flink.util;

import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.datacalc.flink.model.definition.BaseColumn;

import java.util.Map;

public class ColumnUtils {
    public static <T extends BaseColumn> T getComputedColumn(Map<String, ?> columnDefinition, Class<T> columnType) {
        return JsonUtils.convertValue(columnDefinition, columnType);
    }
}
