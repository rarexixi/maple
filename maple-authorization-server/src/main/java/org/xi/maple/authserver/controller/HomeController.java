package org.xi.maple.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.authserver.service.MapleUserService;

import java.util.List;

@RestController
public class HomeController {

    final MapleUserService mapleUserService;

    public HomeController(MapleUserService mapleUserService) {
        this.mapleUserService = mapleUserService;
    }

    @GetMapping(value = "list")
    public ResponseEntity<Integer> select() {
        return ResponseEntity.ok(0);
    }

    @GetMapping(value = "user/{id}/permissions")
    @PreAuthorize("hasAuthority('user:permissions')")
    public ResponseEntity<List<String>> getUserPermissions(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(mapleUserService.getUserPermissionsById(id));
    }
}