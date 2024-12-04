package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class ClusterQueryReq extends QueryReq {

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private Integer disabled;

    public void setNameSort(SortConstants sortConstants)  {
        super.orderBy("name", sortConstants);
    }

    public void getNameSort()  {
        super.getOrderBy().getOrDefault("name", null);
    }
}
