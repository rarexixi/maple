package org.xi.maple.authserver.service;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {
    String createJwt(String subject, Map<String, Object> claims);
    Claims parseJWT(String jwt);
}
