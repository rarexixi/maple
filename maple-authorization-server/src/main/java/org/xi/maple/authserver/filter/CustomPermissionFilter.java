package org.xi.maple.authserver.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class CustomPermissionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println(request.getRequestURI());
    }

    // private PermissionService permissionService;
    //
    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    //         throws ServletException, IOException {
    //
    //     String url = request.getRequestURI();
    //     HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
    //
    //     // 查询数据库获得对应 URL 和 HTTP 方法的权限列表
    //     List<Permission> permissions = permissionService.getPermissionsForUrl(url, httpMethod);
    //
    //     // 检查当前用户是否有足够的权限
    //     boolean hasPermission = checkUserPermissions(permissions);
    //
    //     if (hasPermission) {
    //         filterChain.doFilter(request, response);
    //     } else {
    //         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    //     }
    // }
    //
    // private boolean checkUserPermissions(List<Permission> permissions) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    //
    //     return permissions.stream()
    //             .anyMatch(permission -> authorities.contains(new SimpleGrantedAuthority(permission.getRole())));
    // }
}