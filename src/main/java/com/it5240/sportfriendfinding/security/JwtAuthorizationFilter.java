package com.it5240.sportfriendfinding.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.it5240.sportfriendfinding.entity.User;
import com.it5240.sportfriendfinding.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if(header != null && header.startsWith(JwtProperties.TOKEN_PREFIX)){
            Authentication auth = getUsernamePasswordAuthentication(header);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(String header){
        String token = header.replace(JwtProperties.TOKEN_PREFIX, "");
        String phoneNumber = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token)
                .getSubject();
        if(phoneNumber == null) return null;

        Optional<User> userOpt = userRepository.findById(phoneNumber);

        if(userOpt.isEmpty()) return null;

        CustomUserDetail principal = new CustomUserDetail(userOpt.get());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(phoneNumber, null, principal.getAuthorities());
        return auth;
    }
}
