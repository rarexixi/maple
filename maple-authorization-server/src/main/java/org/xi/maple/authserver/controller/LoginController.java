package org.xi.maple.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.authserver.model.request.LoginReq;
import org.xi.maple.authserver.model.response.LoginResp;
import org.xi.maple.authserver.service.LoginService;

@RestController
public class LoginController {

    final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResp> login(@RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(loginService.login(loginReq));
    }
}
