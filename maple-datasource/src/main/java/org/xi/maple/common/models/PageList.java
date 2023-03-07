package org.xi.maple.common.models;

import java.io.Serializable;
import java.util.Collection;

public class PageList<T> implements Serializable {

    public PageList() {
    }

    public PageList(Integer pageNum, Integer pageSize, Long total, Collection<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Collection<T> list;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Collection<T> getList() {
        return list;
    }

    public void setList(Collection<T> list) {
        this.list = list;
    }
}
