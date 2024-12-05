package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class ClusterQueryReq extends QueryReq {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String address;

    private Collection<String> addressIn;

    private String addressContains;

    private Integer disabled;

    public void setIdRange(Integer[] idRange)  {
        if (idRange == null || idRange.length != 2) {
            return;
        }
        this.idMin = idRange[0];
        this.idMax = idRange[1];
    }

    public void setIdSort(SortConstants sortConstants)  {
        super.orderBy("id", sortConstants);
    }

    public void getIdSort()  {
        super.getOrderBy().getOrDefault("id", null);
    }

    public void setAddressSort(SortConstants sortConstants)  {
        super.orderBy("address", sortConstants);
    }

    public void getAddressSort()  {
        super.getOrderBy().getOrDefault("address", null);
    }
}
