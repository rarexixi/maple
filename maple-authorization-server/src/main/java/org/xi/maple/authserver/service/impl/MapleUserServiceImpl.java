package org.xi.maple.authserver.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xi.maple.authserver.model.MapleUser;
import org.xi.maple.authserver.persistence.entity.UserEntity;
import org.xi.maple.authserver.persistence.mapper.UserMapper;
import org.xi.maple.authserver.service.MapleUserService;

import java.util.List;

@Service
public class MapleUserServiceImpl implements MapleUserService {

    final UserMapper userMapper;

    public MapleUserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userMapper.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        MapleUser userDetails = new MapleUser();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        // userDetails.setAccountNonExpired(user.isAccountNonExpired());
        // userDetails.setAccountNonLocked(user.isAccountNonLocked());
        // userDetails.setCredentialsNonExpired(user.isCredentialsNonExpired());
        // userDetails.setEnabled(user.isEnabled());

        return userDetails;
    }

    @Override
    public MapleUser getUserById(Integer id) {
        UserEntity user = userMapper.getById(id);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        MapleUser userDetails = new MapleUser();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setPermissions(userMapper.getUserPermissionsByUserId(user.getId()));
        // userDetails.setAccountNonExpired(user.isAccountNonExpired());
        // userDetails.setAccountNonLocked(user.isAccountNonLocked());
        // userDetails.setCredentialsNonExpired(user.isCredentialsNonExpired());
        // userDetails.setEnabled(user.isEnabled());
        return userDetails;
    }

    @Override
    public List<String> getUserPermissionsById(Integer id) {
        return userMapper.getUserPermissionsByUserId(id);
    }


}
