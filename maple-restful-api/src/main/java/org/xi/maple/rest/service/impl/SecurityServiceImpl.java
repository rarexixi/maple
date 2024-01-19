package org.xi.maple.rest.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xi.maple.common.exception.MapleAuthenticationException;
import org.xi.maple.common.util.SecurityUtils;
import org.xi.maple.rest.configuration.properties.MapleSecurityProperties;
import org.xi.maple.rest.service.MapleAppService;
import org.xi.maple.rest.service.SecurityService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class SecurityServiceImpl implements SecurityService {

    final MapleAppService mapleAppService;

    final MapleSecurityProperties securityProperties;

    public SecurityServiceImpl(MapleAppService mapleAppService, MapleSecurityProperties securityProperties) {
        this.mapleAppService = mapleAppService;
        this.securityProperties = securityProperties;
    }

    @Override
    public boolean authenticate(String fromApp, String secret, Long timestamp, String secretStr) {
        if (!Boolean.TRUE.equals(securityProperties.isAuth())) {
            return true;
        }
        String secretKey;
        if (StringUtils.isBlank(fromApp) || StringUtils.isBlank(secretKey = mapleAppService.getAppKey(fromApp))) {
            throw new MapleAuthenticationException("应用不存在/设置不正确");
        }
        if (System.currentTimeMillis() - timestamp > 1000 * securityProperties.getAuthExpireSeconds()) {
            throw new MapleAuthenticationException("请求已过期");
        }

        try {
            return SecurityUtils.valid(secretKey, secretStr, secret);
        } catch (NoSuchAlgorithmException | InvalidKeyException ignored) {
            return false;
        }
    }
}
