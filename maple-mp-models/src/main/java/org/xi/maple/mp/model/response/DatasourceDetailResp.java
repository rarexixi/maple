package org.xi.maple.mp.model.response;

import lombok.Data;

@Data
public class DatasourceDetailResp extends DatasourceItemResp {

    /**
     * 数据源类型
     */
    private String datasourceTypeText;
}
