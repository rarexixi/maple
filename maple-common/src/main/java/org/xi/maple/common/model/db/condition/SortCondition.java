package org.xi.maple.common.model.db.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.exception.MapleParamErrorException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortCondition implements Serializable {

    private Map<String, String> orderBy;

    public SortCondition() {
        this.orderBy = new LinkedHashMap<>();
    }

    public SortCondition(Map<String, String> orderBy) {
        this.orderBy = orderBy;
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
        orderBy(key1, sortConstants1);
        orderBy(key2, sortConstants2);
    }

    public void orderBy(String key1, SortConstants sortConstants1, String key2, SortConstants sortConstants2, String key3, SortConstants sortConstants3) {
        orderBy(key1, sortConstants1);
        orderBy(key2, sortConstants2);
        orderBy(key3, sortConstants3);
    }

    public void orderBy(Sort s) {
        if (s.isValid()) {
            throw new MapleParamErrorException("排序字段和规则不能为空");
        }
        this.orderBy.put(s.key, s.sortConstants.name());
    }

    public void orderBy(Sort s1, Sort s2) {
        orderBy(s1);
        orderBy(s2);
    }

    public void orderBy(Sort s1, Sort s2, Sort s3) {
        orderBy(s1);
        orderBy(s2);
        orderBy(s3);
    }

    public void orderBy(Sort... sa) {
        for (Sort sort : sa) {
            orderBy(sort);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Sort {

        private String key;
        private SortConstants sortConstants;

        public static Sort get(String key, SortConstants sortConstants) {
            return new Sort(key, sortConstants);
        }

        private boolean isValid() {
            return StringUtils.isNotBlank(key) && sortConstants != null;
        }
    }
}
