package com.it5240.sportfriendfinding.security;

public class JwtProperties {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION = 86400000;
    public static final String SECRET = "BIMAT";
}
