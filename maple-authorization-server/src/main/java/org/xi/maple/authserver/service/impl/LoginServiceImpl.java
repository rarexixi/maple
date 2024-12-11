package org.xi.maple.authserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.xi.maple.authserver.model.MapleUser;
import org.xi.maple.authserver.model.request.LoginReq;
import org.xi.maple.authserver.model.response.LoginResp;
import org.xi.maple.authserver.service.JwtService;
import org.xi.maple.authserver.service.LoginService;
import org.xi.maple.authserver.util.JwtUtils;

@Service
public class LoginServiceImpl implements LoginService {

    final AuthenticationManager authenticationManager;
    final JwtService jwtService;

    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResp login(LoginReq loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        MapleUser user = (MapleUser) authentication.getPrincipal();
        String jwt = jwtService.createJwt(user.getId().toString(), user.getClaims());
        // todo 缓存 user

        return new LoginResp(jwt);
    }
}
