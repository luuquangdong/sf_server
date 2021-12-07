package com.it5240.sportfriendfinding.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.it5240.sportfriendfinding.dto.LoginRequest;
import com.it5240.sportfriendfinding.security.CustomUserDetailService;
import com.it5240.sportfriendfinding.security.JwtProperties;
import com.it5240.sportfriendfinding.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginInfo){
        String token = authService.login(loginInfo);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

        String body = "{\"token\":\"" + token + "\"}";

        return ResponseEntity.ok().headers(responseHeaders).body(body);
    }

    @PostMapping("/admin_login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginInfo){
        String token = authService.adminLogin(loginInfo);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

        String body = "{\"token\":\"" + token + "\"}";

        return ResponseEntity.ok().headers(responseHeaders).body(body);
    }
}