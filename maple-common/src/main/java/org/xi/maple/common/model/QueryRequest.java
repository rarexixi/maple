package org.xi.maple.common.model;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.exception.MapleParamErrorException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xishihao
 */
public abstract class QueryRequest implements Serializable {

    final Map<String, String> orderBy = new LinkedHashMap<>();

    public Map<String, String> getOrderBy() {
        return orderBy;
    }

    protected void orderBy(String key, SortConstants sortConstants) {
        if (StringUtils.isBlank(key) || sortConstants == null) {
            throw new MapleParamErrorException("排序字段和规则不能为空");
        }
        this.orderBy.put(key, sortConstants.name());
    }
}
