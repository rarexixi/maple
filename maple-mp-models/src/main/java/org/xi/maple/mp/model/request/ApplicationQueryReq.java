package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class ApplicationQueryReq extends QueryReq {

    private String appName;

    private Collection<String> appNameIn;

    private String appNameContains;

    private Integer disabled;

    public void setAppNameSort(SortConstants sortConstants)  {
        super.orderBy("app_name", sortConstants);
    }

    public void getAppNameSort()  {
        super.getOrderBy().getOrDefault("app_name", null);
    }
}
