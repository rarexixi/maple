package org.xi.maple.persistence.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryRequest;

import java.util.Collection;

@Data
public class ClusterQueryRequest extends QueryRequest {

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private String category;

    private Integer deleted;

    public void setNameSort(SortConstants sortConstants)  {
        super.orderBy("name", sortConstants);
    }

    public void getNameSort()  {
        super.getOrderBy().getOrDefault("name", null);
    }
}
