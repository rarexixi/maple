package org.xi.maple.authserver.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.xi.maple.authserver.configuration.properties.MapleJwtProperties;
import org.xi.maple.authserver.service.JwtService;
import org.xi.maple.authserver.util.JwtUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtUtils jwtUtils;

    public JwtServiceImpl(MapleJwtProperties mapleJwtProperties) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.jwtUtils = new JwtUtils(mapleJwtProperties.privateKey, mapleJwtProperties.publicKey);
    }

    @Override
    public String createJwt(String subject, Map<String, Object> claims) {
        return jwtUtils.createJwt(subject, claims);
    }

    @Override
    public Claims parseJWT(String jwt) {
        return jwtUtils.parseJWT(jwt);
    }
}
