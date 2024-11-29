package org.xi.maple.persistence.model.request;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.util.Collection;

import lombok.Data;

@Data
public class ClusterQueryReq extends QueryReq {

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private String category;

    private Integer disabled;

    public void setNameSort(SortConstants sortConstants)  {
        super.orderBy("name", sortConstants);
    }

    public void getNameSort()  {
        super.getOrderBy().getOrDefault("name", null);
    }
}
