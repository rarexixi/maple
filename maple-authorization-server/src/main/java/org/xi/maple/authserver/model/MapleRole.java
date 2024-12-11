package org.xi.maple.authserver.model;

import org.springframework.security.core.GrantedAuthority;

public class MapleRole implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
        return "";
    }
}
