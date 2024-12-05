package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class SysConfQueryReq extends QueryReq {

    private String confKey;

    private Collection<String> confKeyIn;

    private String confKeyContains;

    private Integer disabled;

    public void setConfKeySort(SortConstants sortConstants)  {
        super.orderBy("conf_key", sortConstants);
    }

    public void getConfKeySort()  {
        super.getOrderBy().getOrDefault("conf_key", null);
    }
}
