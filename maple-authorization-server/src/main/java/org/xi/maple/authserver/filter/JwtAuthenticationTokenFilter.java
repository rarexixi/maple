package org.xi.maple.authserver.filter;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xi.maple.authserver.model.MapleUser;
import org.xi.maple.authserver.service.JwtService;
import org.xi.maple.authserver.service.MapleUserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    final JwtService jwtService;
    final MapleUserService userService;

    public JwtAuthenticationTokenFilter(JwtService jwtService, MapleUserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        String token = req.getHeader("token");
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(req, resp);
            return;
        }
        String userId;
        try {
            Claims claims = jwtService.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        MapleUser user = userService.getUserById(Integer.parseInt(userId)); //todo 获取用户具体信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(req, resp);
    }
}
