package org.xi.maple.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static String getParam(HttpServletRequest request, String name) {
        String result;
        if (StringUtils.isNotBlank(result = request.getParameter(name))) {
            return result;
        }
        if (StringUtils.isNotBlank(result = request.getHeader(name))) {
            return result;

        }
        Cookie tokenCookie = RequestUtils.getCookie(request, name);
        return tokenCookie == null ? "" : tokenCookie.getValue();
    }
}