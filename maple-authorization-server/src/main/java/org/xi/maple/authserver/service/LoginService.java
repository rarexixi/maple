package org.xi.maple.authserver.service;

import org.xi.maple.authserver.model.request.LoginReq;
import org.xi.maple.authserver.model.response.LoginResp;

public interface LoginService {
    LoginResp login(LoginReq loginReq);
}
