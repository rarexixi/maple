package org.xi.maple.authserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xi.maple.authserver.model.MapleUser;

import java.util.List;

public interface MapleUserService extends UserDetailsService {
    MapleUser getUserById(Integer id);
    List<String > getUserPermissionsById(Integer id);
}
