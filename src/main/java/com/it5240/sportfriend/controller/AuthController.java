package com.it5240.sportfriend.controller;

import com.it5240.sportfriend.model.dto.auth.AuthInfo;
import com.it5240.sportfriend.model.dto.auth.LoginResp;
import com.it5240.sportfriend.model.dto.auth.SignUpInfo;
import com.it5240.sportfriend.service.AuthService;
import com.it5240.sportfriend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthInfo loginInfo){
        LoginResp loginResponse = authService.login(loginInfo);
        loginResponse.getUser().setCanEdit(true);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtUtil.AUTH_HEADER, JwtUtil.TOKEN_PREFIX + loginResponse.getToken());

        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @PostMapping("/admin-login")
    public ResponseEntity<?> adminLogin(@RequestBody AuthInfo loginInfo){
        LoginResp loginResponse = authService.adminLogin(loginInfo);
        loginResponse.getUser().setCanEdit(true);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtUtil.AUTH_HEADER, JwtUtil.TOKEN_PREFIX + loginResponse.getToken());

        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpInfo signUpInfo){
        var result = authService.signUp(signUpInfo);
        return ResponseEntity.ok(result);
    }

}