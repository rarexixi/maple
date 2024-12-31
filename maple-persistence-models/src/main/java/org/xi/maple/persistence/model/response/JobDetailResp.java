package org.xi.maple.persistence.model.response;

import lombok.Data;

@Data
public class JobDetailResp extends JobItemResp {

    /**
     * 引擎ID
     */
    private String engineText;
}
