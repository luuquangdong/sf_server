package com.it5240.sportfriendfinding.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it5240.sportfriendfinding.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    /* kích hoạt khi có request gửi đến /login (default) */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String phoneNumber = "0";
        String password = "1";
        LoginRequest credentials = null;
        try {
            credentials =  this.objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            phoneNumber = credentials.getPhoneNumber();
            password = credentials.getPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,
                password,
                new ArrayList<>()
        );

        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail userPrincipal = (CustomUserDetail) authResult.getPrincipal();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JwtProperties.EXPIRATION);

        String token = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
        response.addHeader("access-control-expose-headers", "Authorization");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = "{\"role\":\"" + userPrincipal.getRole() + "\"}";

        PrintWriter out = response.getWriter();
        out.print(body);
    }
}
