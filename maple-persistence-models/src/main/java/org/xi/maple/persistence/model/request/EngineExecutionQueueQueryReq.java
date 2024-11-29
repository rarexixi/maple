package org.xi.maple.persistence.model.request;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.util.Collection;

import lombok.Data;

@Data
public class EngineExecutionQueueQueryReq extends QueryReq {

    private String queueName;

    private Collection<String> queueNameIn;

    private String queueNameContains;

    public void setQueueNameSort(SortConstants sortConstants)  {
        super.orderBy("queue_name", sortConstants);
    }

    public void getQueueNameSort()  {
        super.getOrderBy().getOrDefault("queue_name", null);
    }
}
