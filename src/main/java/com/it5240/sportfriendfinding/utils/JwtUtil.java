package com.it5240.sportfriendfinding.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    @Value("${jwt.token-expiration}")
    private long expiration;
    @Value("${jwt.token-secret}")
    private String tokenSecret;
    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    public String generateToken(String userId){
        Date expireDate = new Date(System.currentTimeMillis() + this.expiration);

        String token = JWT.create()
                .withSubject(userId)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC512(this.tokenSecret));

        return token;
    }

    public String generateRefreshToken(String userId){
        String refreshToken = JWT.create()
                .withSubject(userId)
                .sign(Algorithm.HMAC512(this.refreshTokenSecret));
        return refreshToken;
    }

    public String getSubjectFromToken(String token){
        String userId = JWT.require(Algorithm.HMAC512(this.tokenSecret))
                .build()
                .verify(token)
                .getSubject();
        return userId;
    }

    public String getSubjectFromRefreshToken(String refreshToken){
        String userId = JWT.require(Algorithm.HMAC512(this.refreshTokenSecret))
                .build()
                .verify(refreshToken)
                .getSubject();
        return userId;
    }
}
