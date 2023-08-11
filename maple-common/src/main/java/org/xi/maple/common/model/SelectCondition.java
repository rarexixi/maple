package org.xi.maple.common.model;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.exception.MapleParamErrorException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 数据库查询条件
 *
 * @author xishihao
 */
public abstract class SelectCondition implements Serializable {

    final List<String> columns = new LinkedList<>();
    private Map<String, String> orderBy = new LinkedHashMap<>();

    public List<String> getColumns() {
        return columns.isEmpty() ? null : columns;
    }

    public void setSelectColumns(String... columns) {
        Collections.addAll(this.columns, columns);
    }

    public Map<String, String> getOrderBy() {
        return orderBy == null || orderBy.isEmpty() ? null : orderBy;
    }

    public void setOrderBy(Map<String, String> orderBy) {
        this.orderBy = orderBy;
    }

    public void orderBy(String key, SortConstants sortConstants) {
        if (StringUtils.isBlank(key) || sortConstants == null) {
            throw new MapleParamErrorException("排序字段和规则不能为空");
        }
        this.orderBy.put(key, sortConstants.name());
    }

    public void orderBy(String key1, SortConstants sortConstants1, String key2, SortConstants sortConstants2) {
        if (StringUtils.isBlank(key1) || sortConstants1 == null
                || StringUtils.isBlank(key2) || sortConstants2 == null) {
            throw new MapleParamErrorException("排序字段和规则不能为空");
        }
        this.orderBy.put(key1, sortConstants1.name());
        this.orderBy.put(key2, sortConstants2.name());
    }

    public void orderBy(String key1, SortConstants sortConstants1, String key2, SortConstants sortConstants2, String key3, SortConstants sortConstants3) {
        if (StringUtils.isBlank(key1) || sortConstants1 == null
                || StringUtils.isBlank(key2) || sortConstants2 == null
                || StringUtils.isBlank(key3) || sortConstants3 == null) {
            throw new MapleParamErrorException("排序字段和规则不能为空");
        }
        this.orderBy.put(key1, sortConstants1.name());
        this.orderBy.put(key2, sortConstants2.name());
        this.orderBy.put(key3, sortConstants3.name());
    }
}
