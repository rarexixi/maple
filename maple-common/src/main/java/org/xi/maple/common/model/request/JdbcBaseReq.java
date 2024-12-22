package org.xi.maple.common.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public abstract class JdbcBaseReq implements Serializable {
    String url;
    String username;
    String password;
    String driverClassName;

    public abstract Map<String, ?> getSqlParamMap();
}
