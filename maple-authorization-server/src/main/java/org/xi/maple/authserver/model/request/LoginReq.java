package org.xi.maple.authserver.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginReq implements Serializable {
    private String username;
    private String password;
}
